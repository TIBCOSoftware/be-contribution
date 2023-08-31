package com.tibco.cep.analytics.pmml.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.jpmml.evaluator.Computable;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.OutputField;
import org.jpmml.evaluator.TargetField;
import org.jpmml.evaluator.visitors.MiningFieldInterner;
import org.jpmml.evaluator.visitors.PredicateInterner;
import org.jpmml.evaluator.visitors.ScoreDistributionInterner;
import org.jpmml.model.PMMLUtil;
import org.jpmml.model.visitors.ArrayListTransformer;
import org.jpmml.model.visitors.DoubleInterner;
import org.jpmml.model.visitors.IntegerInterner;
import org.jpmml.model.visitors.StringInterner;
import org.jpmml.evaluator.visitors.ValueOptimizer;
import org.jpmml.model.visitors.FloatInterner;
import org.jpmml.model.visitors.LocatorTransformer;
import org.jpmml.model.visitors.NodeScoreOptimizer;
import com.tibco.cep.analytics.pmml.io.utils.Helper;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.element.Concept;
import com.tibco.cep.runtime.model.event.SimpleEvent;

public class PmmlFunctionsDelegate {
    private static ConcurrentHashMap<String, Evaluator> pmmlModelMap = new ConcurrentHashMap<>();
    static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(PmmlFunctionsDelegate.class);

    public static void loadModel(String modelName, String filePath, boolean optimize) {
        try {
            File pmmlFile = new File(filePath);
            PMML pmml = readPMML(pmmlFile);
            if (pmml.getHeader().getApplication().getName() == null) {
            	throw new RuntimeException("Attribute 'name' of Application is not specified");
            }
            List<Model> models = pmml.getModels();
            for (Model model:models) {
            	if(model.getMiningFunction() == null) {
            		throw new RuntimeException("Attribute 'functionName' of TreeModel is not specified");	
            	}
            }
            ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
            Evaluator evaluator = modelEvaluatorFactory.newModelEvaluator(pmml);
            evaluator.verify();

            if (optimize) {
                for (Visitor optimizer : getOptimizers()) {
                    optimizer.applyTo(pmml);
                }
                for (Visitor interner : getInterners()) {
                    interner.applyTo(pmml);
                }

                for (Visitor transformer : getTransformers()) {
                    transformer.applyTo(pmml);
                }
            }
            pmmlModelMap.put(modelName, evaluator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean modelExists(String modelName) {
        return pmmlModelMap.containsKey(modelName);
    }

    public static void removeModel(String modelName) {
        Evaluator ev = pmmlModelMap.remove(modelName);
        if (ev == null) {
            throw new RuntimeException("Model does not exist");
        }
    }

    private static Object evaluate(String modelName, Map<FieldName, Object> parameters) {
        Evaluator ev = getEvaluator(modelName);
        try {
            Map<FieldName, ?> result = ev.evaluate(parameters);
            Map<String, ?> retResult = convertMapToStringKeys(result);
            return retResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, ?> convertMapToStringKeys(Map<FieldName, ?> result) {
        Map<String, Object> map = new HashMap<String, Object>(result.size());
        for (Entry<FieldName, ?> entry : result.entrySet()) {
            if (entry.getValue() instanceof Computable) {
                map.put(entry.getKey().toString(), ((Computable) entry.getValue()).getResult());
            } else {
                map.put(entry.getKey().toString(), entry.getValue());
            }
        }
        return map;
    }

    public static Object evalModelwithParams(String modelName, Object... parameters) {
        if ((parameters.length % 2) != 0) {
            throw new RuntimeException("The numbers of input parameters of callModel should be even!");
        }
        try {
            Map<FieldName, Object> input = new LinkedHashMap<>();
            boolean flag = true;
            String fieldName = null;
            for (Object par : parameters) {
                if (flag) {
                    if (!(par instanceof String)) {
                        throw new RuntimeException("The input format of callMode is not correct!+");
                    }
                    fieldName = (String) par;
                } else {
                	FieldName field = FieldName.create(fieldName);
                	input.put(field, par);
                }
                flag = !flag;
            }
            return evaluate(modelName, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object evalModelWithConcept(String modelName, Concept concept) {
        try {
            return evalModelwithParams(modelName, Helper.toParameters(concept));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object evalModelwithEvent(String modelName, SimpleEvent event) {
        try {
            return evalModelwithParams(modelName, Helper.toParameters(event));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getTargetFields(String modelName) {
        Evaluator evaluator = getEvaluator(modelName);
        List<TargetField> targetFields = evaluator.getTargetFields();
        List<String> targetFieldsStrings = new ArrayList<String>(targetFields.size());
        for (TargetField targetField : targetFields) {
            targetFieldsStrings.add(targetField.getName().toString());
        }
        return targetFieldsStrings;
    }

    public static Object getInputFields(String modelName) {
        Evaluator evaluator = getEvaluator(modelName);
        List<InputField> inputFields = evaluator.getInputFields();
        List<String> inputFieldsStrings = new ArrayList<String>(inputFields.size());
        for (InputField inputField : inputFields) {
            inputFieldsStrings.add(inputField.getName().toString());
        }
        return inputFieldsStrings;
    }


    private static Evaluator getEvaluator(String modelName) {
        Evaluator evaluator = pmmlModelMap.get(modelName);
        if (evaluator == null) {
            throw new RuntimeException("Cannot find model, " + modelName);
        }
        return evaluator;
    }

    public static Object getOutputFields(String modelName) {
        Evaluator evaluator = getEvaluator(modelName);
        List<OutputField> outputFields = evaluator.getOutputFields();
        List<String> outputFieldsStrings = new ArrayList<String>(outputFields.size());
        for (OutputField outputField : outputFields) {
            outputFieldsStrings.add(outputField.getName().toString());
        }
        return outputFieldsStrings;
    }
    
    
    private static List<? extends Visitor> getOptimizers(){
        return Arrays.asList(new ValueOptimizer(), new NodeScoreOptimizer());
    }
    private static List<? extends Visitor> getInterners(){
        return Arrays.asList(new DoubleInterner(), new IntegerInterner(), new StringInterner(), new FloatInterner(), new MiningFieldInterner(), new PredicateInterner(), new ScoreDistributionInterner());
    }
    
    private static List<? extends Visitor> getTransformers(){
        return Arrays.asList(new ArrayListTransformer(), new LocatorTransformer());
    }
    
    private static PMML readPMML(File file) throws Exception {
        try(InputStream is = new FileInputStream(file)){
            return PMMLUtil.unmarshal(is);
        }
    }
}

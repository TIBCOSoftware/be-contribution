package  com.tibco.be.custom.amazonaws.services.s3;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;
import static com.tibco.be.model.functions.FunctionDomain.BUI;

import com.tibco.be.custom.amazonaws.services.auth.AWS4SignerBase;
import com.tibco.be.custom.amazonaws.services.auth.AWS4SignerForAuthorizationHeader;
import com.tibco.be.custom.amazonaws.services.util.BinaryUtils;
import com.tibco.be.custom.amazonaws.services.util.HttpUtils;
import com.tibco.be.model.functions.BEFunction;
import com.tibco.be.model.functions.BEMapper;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@BEPackage(
        catalog = "AmazonAWS",//Add a catalog name here
        category = "S3", //Add a category name here
        synopsis = "") //Add a synopsis here
public class DeleteS3Object{

    @BEFunction(
            name = "deleteS3Object",
            signature = "String deleteS3Object (String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)",
            params = {
                    @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
                    @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
                    @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
                    @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
                    @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
            },
            freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
            version = "1.0", /*Add Version here*/
            see = "",
            mapper = @BEMapper(),
            description = "Delete an object from S3 Bucket" /*Add Description here*/,
            cautions = "none",
            fndomain = {ACTION, BUI},
            example = "String resultGet = S3.deleteS3Object(\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static String deleteS3Object(String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey) {

        // the region-specific endpoint to the target object expressed in path style
        URL endpointUrl;

        try {
            endpointUrl = new URL("https://" + bucketName + ".s3.amazonaws.com/"+objectName);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to parse service endpoint: " + e.getMessage());
        }

        // for a simple DELETE, we have no body so supply the precomputed 'empty' hash
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-amz-content-sha256", AWS4SignerBase.EMPTY_BODY_SHA256);

        AWS4SignerForAuthorizationHeader signer = new AWS4SignerForAuthorizationHeader(
                endpointUrl, "DELETE", "s3", regionName);
        String authorization = signer.computeSignature(headers,
                null, // no query parameters
                AWS4SignerBase.EMPTY_BODY_SHA256,
                awsAccessKey,
                awsSecretKey);

        // place the computed signature into a formatted 'Authorization' header
        // and call S3
        headers.put("Authorization", authorization);
        String response = HttpUtils.invokeHttpRequest(endpointUrl, "DELETE", headers, null);
        return response;
    }
}
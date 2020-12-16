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
public class PutS3Object{
      
   @BEFunction(
			name = "putS3Object",
			signature = "String putS3Object (String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey, String objectContent)",
			params = {
					@FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
					@FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
					@FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
					@FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
					@FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
					@FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
			},
			freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
			version = "1.0", /*Add Version here*/
			see = "",
			mapper = @BEMapper(),
			description = "Put an object to a S3 Bucket" /*Add Description here*/,
			cautions = "none",
			fndomain = {ACTION, BUI},
			example = "String resultPut = S3.putS3Object(\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\",objectContent);\n"
			)
	public static String putS3Object(String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey, String objectContent) {
		
       URL endpointUrl;
       try {          
           if (regionName.equals("us-east-1")) {
               endpointUrl = new URL("https://s3.amazonaws.com/" + bucketName + "/"+objectName);
           } else {
               endpointUrl = new URL("https://s3-" + regionName + ".amazonaws.com/" + bucketName + "/"+objectName);
           }
     
       } catch (MalformedURLException e) {
           throw new RuntimeException("Unable to parse service endpoint: " + e.getMessage());
       }
       
       // precompute hash of the body content
       byte[] contentHash = AWS4SignerBase.hash(objectContent);
       String contentHashString = BinaryUtils.toHex(contentHash);
       
       Map<String, String> headers = new HashMap<String, String>();
       headers.put("x-amz-content-sha256", contentHashString);
       headers.put("content-length", "" + objectContent.length());
       headers.put("x-amz-storage-class", "REDUCED_REDUNDANCY");
       
       AWS4SignerForAuthorizationHeader signer = new AWS4SignerForAuthorizationHeader(
               endpointUrl, "PUT", "s3", regionName);
       String authorization = signer.computeSignature(headers, 
                                                      null, // no query parameters
                                                      contentHashString, 
                                                      awsAccessKey, 
                                                      awsSecretKey);
               
       // express authorization for this as a header
       headers.put("Authorization", authorization);
       
       // make the call to Amazon S3
       String response = HttpUtils.invokeHttpRequest(endpointUrl, "PUT", headers, objectContent);
       
       return response;
	}
	
}
/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.custom.aws.services.s3;

import static com.tibco.be.custom.aws.services.util.SAMLUtils.createCredentialsWithSAML;
import static com.tibco.be.model.functions.FunctionDomain.ACTION;
import static com.tibco.be.model.functions.FunctionDomain.BUI;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.SSEAwsKeyManagementParams;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.util.StringUtils;
import com.tibco.be.custom.aws.services.util.StsUtils;
import com.tibco.be.model.functions.BEFunction;
import com.tibco.be.model.functions.BEMapper;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

@BEPackage(
        catalog = "AWS",//Add a catalog name here
        category = "S3", //Add a category name here
        synopsis = "AWS S3 Functions") //Add a synopsis here
public class Bucket {


    //-------------------------------- Default Credential Chain --------------------------------

    @BEFunction(
        name = "deleteBucket",
        signature = "void deleteBucket(String endpoint, String bucketName, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Delete a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "S3.deleteBucket(null,\"my-bucket\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static void deleteBucket(String endpoint, String bucketName, String regionName) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        deleteBucket(client, bucketName );
        client.shutdown();

    }

    @BEFunction(
        name = "createBucket",
        signature = "String createBucket (String endpoint, String bucketName, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Create a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String name = S3.createBucket(null,\"my-bucket\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static String createBucket(String endpoint, String bucketName, String regionName) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        String name = createBucket(client, bucketName);
        client.shutdown();
        return name;
    }

    @BEFunction(
        name = "doesBucketExist",
        signature = "boolean doesBucketExist (String endpoint, String bucketName, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Does a S3 Bucket exist" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesBucketExist(null,\"my-bucket\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static boolean doesBucketExist(String endpoint, String bucketName, String regionName) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        boolean exists =  doesBucketExist(client, bucketName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "doesObjectExist",
        signature = "boolean doesObjectExist (String endpoint, String bucketName, String objectName, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Does Object exist in S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesObjectExist(null,\"my-bucket\",\"my-object\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static boolean doesObjectExist(String endpoint, String bucketName, String objectName, String regionName) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        boolean exists =  doesObjectExist(client, bucketName, objectName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "deleteS3Object",
        signature = "void deleteS3Object (String endpoint, String bucketName, String objectName, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Delete an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "S3.deleteS3Object(null,\"my-bucket\",\"my-object\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static void deleteS3Object(String endpoint, String bucketName, String objectName, String regionName) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        deleteObject(client, bucketName, objectName);
        client.shutdown();

    }

    @BEFunction(
        name = "putS3Object",
        signature = "String putS3Object (String endpoint, String bucketName, String objectName, String objectContent, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String result = S3.putS3Object(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3Object(String endpoint, String bucketName, String objectName, String objectContent, String regionName) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        String result = putObject(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "putS3ObjectUsingSSES3",
        signature = "String putS3ObjectUsingSSES3(String endpoint, String bucketName,  String objectName, String objectContent, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingSSES3(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSES3(String endpoint, String bucketName,  String objectName, String objectContent, String regionName) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        String result = putObjectWithSSE_S3(client, bucketName, objectName,  objectContent);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "putS3ObjectUsingSSEKMS",
        signature = "String putS3ObjectUsingSSEKMS(String endpoint, String bucketName,  String objectName, String objectContent, String kmsKeyID, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsKeyID", type = "String", desc = "KMS Key ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingSSEKMS(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSEKMS(String endpoint, String bucketName,  String objectName, String objectContent, String kmsKeyID, String regionName) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        String result = putObjectWithSSE_KMS(client, bucketName, objectName, kmsKeyID, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "putS3ObjectUsingCSEKMS",
        signature = "String putS3ObjectUsingCSEKMS(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsKeyID", type = "String", desc = "AWS KMS Key ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingCSEKMS(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingCSEKMS(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName) throws RuntimeException {

        AmazonS3EncryptionV2 client = createS3Encryption(endpoint, regionName, kmsKeyID);
        String result = putObjectWithCSE_KMS(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "getS3Object",
        signature = "String getS3Object (String endpoint, String bucketName, String objectName, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Get an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultGet = S3.getS3Object(null,\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static String getS3Object(String endpoint, String bucketName, String objectName, String regionName) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        String result =  getObject(client, bucketName, objectName);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "getS3Object2",
        signature = "Object getS3Object2(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Get an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "Object obj = S3.getS3Object2(null,\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static Object getS3Object2(String endpoint, String bucketName, String objectName, String regionName) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        Object result =  getObject2(client, bucketName, objectName);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "generatePreSignedUrl",
        signature = "String generatePreSignedUrl (String endpoint, String bucketName, String objectName, Long expTimeDuration, String kmsCmkId, String regionName)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "expTimeDuration", type = "long", desc = "URL Expiration Duration (minutes)" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsCmkId", type = "String", desc = "KMS Custom ID or null" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "Pre-signed URL" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Generate Pre-signed URL" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String url = S3.generatePreSignedUrl(null,\"my-bucket\",\"my-key\", \"eu-west-1\" ...);\r\n"
    )
    public static String generatePreSignedUrl(String endpoint, String bucketName, String objectName, long expTimeDuration, String kmsCmkId, String regionName) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName);
        String result = generatePreSignedUrl(client, bucketName, objectName, kmsCmkId, expTimeDuration);
        client.shutdown();
        return result;


    }


    //--------------------------------  Access Key & Secret  --------------------------------

    @BEFunction(
        name = "deleteBucketWithAccessKeySecret",
        signature = "void deleteBucketWithAccessKeySecret(String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Delete a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "S3.deleteBucketWithAccessKeySecret(null,\"my-bucket\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static void deleteBucketWithAccessKeySecret(String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        deleteBucket(client, bucketName );
        client.shutdown();

    }

    @BEFunction(
        name = "createBucketWithAccessKeySecret",
        signature = "String createBucketWithAccessKeySecret (String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Create a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String name = S3.createBucketWithAccessKeySecret(null,\"my-bucket\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static String createBucketWithAccessKeySecret(String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        String name = createBucket(client, bucketName);
        client.shutdown();
        return name;
    }

    @BEFunction(
        name = "doesBucketExistWithAccessKeySecret",
        signature = "boolean doesBucketExistWithAccessKeySecret (String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Does a S3 Bucket exist" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesBucketExistWithAccessKeySecret(null,\"my-bucket\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static boolean doesBucketExistWithAccessKeySecret(String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        boolean exists =  doesBucketExist(client, bucketName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "doesObjectExistWithAccessKeySecret",
        signature = "boolean doesObjectExistWithAccessKeySecret (String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
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
        description = "Does Object exist in S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesObjectExistWithAccessKeySecret(null,\"my-bucket\",\"my-object\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static boolean doesObjectExistWithAccessKeySecret(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        boolean exists =  doesObjectExist(client, bucketName, objectName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "deleteS3ObjectWithAccessKeySecret",
        signature = "void deleteS3ObjectWithAccessKeySecret (String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
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
        example = "S3.deleteS3ObjectWithAccessKeySecret(null,\"my-bucket\",\"my-object\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static void deleteS3ObjectWithAccessKeySecret(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException  {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        deleteObject(client, bucketName, objectName);
        client.shutdown();

    }

    @BEFunction(
        name = "putS3ObjectWithAccessKeySecret",
        signature = "String putS3ObjectWithAccessKeySecret (String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),

        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String result = S3.putS3ObjectWithAccessKeySecret(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectWithAccessKeySecret(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        String result = putObject(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "putS3ObjectUsingSSES3WithAccessKeySecret",
        signature = "String putS3ObjectUsingSSES3WithAccessKeySecret(String endpoint, String bucketName,  String objectName, String objectContent, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),

        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingSSES3WithAccessKeySecret(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSES3WithAccessKeySecret(String endpoint, String bucketName,  String objectName, String objectContent, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        String result = putObjectWithSSE_S3(client, bucketName, objectName,  objectContent);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "putS3ObjectUsingSSEKMSWithAccessKeySecret",
        signature = "String putS3ObjectUsingSSEKMSWithAccessKeySecret(String endpoint, String bucketName,  String objectName, String objectContent, String kmsKeyID, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsKeyID", type = "String", desc = "KMS Key ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),

        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingSSEKMSWithAccessKeySecret(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSEKMSWithAccessKeySecret(String endpoint, String bucketName,  String objectName, String objectContent, String kmsKeyID, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        String result = putObjectWithSSE_KMS(client, bucketName, objectName, kmsKeyID, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "putS3ObjectUsingCSEKMSWithAccessKeySecret",
        signature = "String putS3ObjectUsingCSEKMSWithAccessKeySecret(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsKeyID", type = "String", desc = "AWS KMS Key ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),

        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingCSEKMSWithAccessKeySecret(null,\"my-bucket\",\"data.xml\", \"hello,world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingCSEKMSWithAccessKeySecret(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3EncryptionV2 client = createS3Encryption(endpoint, regionName, kmsKeyID, awsAccessKey, awsSecretKey);
        String result = putObjectWithCSE_KMS(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "getS3ObjectWithAccessKeySecret",
        signature = "String getS3ObjectWithAccessKeySecret (String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
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
        description = "Get an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultGet = S3.getS3ObjectWithAccessKeySecret(null,\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static String getS3ObjectWithAccessKeySecret(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        String result =  getObject(client, bucketName, objectName);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "getS3Object2WithAccessKeySecret",
        signature = "Object getS3Object2WithAccessKeySecret(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Get an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "Object obj = S3.getS3Object2WithAccessKeySecret(null,\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static Object getS3Object2WithAccessKeySecret(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        Object result =  getObject2(client, bucketName, objectName);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "generatePreSignedUrlWithAccessKeySecret",
        signature = "String generatePreSignedUrlWithAccessKeySecret (String endpoint, String bucketName, String objectName, Long expTimeDuration, String kmsCmkId, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "expTimeDuration", type = "long", desc = "URL Expiration Duration (minutes)" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsCmkId", type = "String", desc = "KMS Custom ID or null" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "Pre-signed URL" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Generate Pre-signed URL" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String url = S3.generatePreSignedUrlWithAccessKeySecret(null,\"my-bucket\",\"my-key\", \"eu-west-1\" ...);\r\n"
    )
    public static String generatePreSignedUrlWithAccessKeySecret(String endpoint, String bucketName, String objectName, long expTimeDuration, String kmsCmkId, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, awsAccessKey, awsSecretKey);
        String result = generatePreSignedUrl(client, bucketName, objectName, kmsCmkId, expTimeDuration);
        client.shutdown();
        return result;


    }

    //-------------------------------- RoleARN --------------------------------

    @BEFunction(
        name = "deleteBucketWithRoleARN",
        signature = "void deleteBucketWithRoleARN (String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Delete a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "S3.deleteBucketWithRoleARN(null,\"my-bucket\", \"eu-west-1\" ...);\n"
    )
    public static void deleteBucketWithRoleARN(String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        deleteBucket(client, bucketName );
        client.shutdown();

    }

    @BEFunction(
        name = "createBucketWithRoleARN",
        signature = "String createBucketWithRoleARN (String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Create a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String name = S3.createBucketWithRoleARN(null,\"my-bucket\", \"eu-west-1\" ...);\n"
    )
    public static String createBucketWithRoleARN(String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        String name = createBucket(client, bucketName);
        client.shutdown();
        return name;
    }

    @BEFunction(
        name = "doesBucketExistWithRoleARN",
        signature = "boolean doesBucketExistWithRoleARN (String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Does S3 Bucket exist" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesBucketExistWithRoleARN(null,\"my-bucket\", \"eu-west-1\" ...);\n"
    )
    public static boolean doesBucketExistWithRoleARN(String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        boolean exists = doesBucketExist(client, bucketName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "doesObjectExistWithRoleARN",
        signature = "boolean doesObjectExistWithRoleARN (String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Does Object exist in S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesObjectExist(null,\"my-bucket\",\"my-object\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static boolean doesObjectExistWithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        boolean exists =  doesObjectExist(client, bucketName, objectName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "deleteS3ObjectWithRoleARN",
        signature = "void deleteS3ObjectWithRoleARN (String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
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
        example = "String resultPut = S3.putS3ObjectWithRoleARN(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static void deleteS3ObjectWithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        deleteObject(client, bucketName, objectName );
        client.shutdown();

    }

    @BEFunction(
        name = "putS3ObjectWithRoleARN",
        signature = "String putS3ObjectWithRoleARN (String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectWithRoleARN(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectWithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        String result = putObject(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "putS3ObjectUsingSSES3WithRoleARN",
        signature = "String putS3ObjectUsingSSES3WithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String result = S3.putS3ObjectUsingSSES3WithRoleARN(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSES3WithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        String result = putObjectWithSSE_S3(client, bucketName, objectName,  objectContent);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "putS3ObjectUsingSSEKMSWithRoleARN",
        signature = "String putS3ObjectUsingSSEKMSWithRoleARN (String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingSSEKMSWithRoleARN(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSEKMSWithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        String result = putObjectWithSSE_KMS(client, bucketName, objectName, kmsKeyID, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "putS3ObjectUsingCSEKMSWithRoleARN",
        signature = "String putS3ObjectUsingCSEKMSWithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsKeyID", type = "String", desc = "KMS Key ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingCSEKMSWithRoleARN(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingCSEKMSWithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {


        AmazonS3EncryptionV2 client = createS3Encryption(endpoint,regionName, kmsKeyID, roleARN,
            roleSessionName,
            duration,
            awsAccessKey, awsSecretKey);

        String result = putObjectWithCSE_KMS(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "getS3ObjectWithRoleARN",
        signature = "String getS3ObjectWithRoleARN (String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String content = S3.getS3ObjectWithRoleARN(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String getS3ObjectWithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        String result = getObject(client, bucketName, objectName);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "getS3Object2WithRoleARN",
        signature = "Object getS3Object2WithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey, String objectContent)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Get an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "Object obj = S3.getS3Object2WithRoleARN(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static Object getS3Object2WithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
        Object result = getObject2(client, bucketName, objectName);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "generatePreSignedUrlWithRoleARN",
        signature = "String generatePreSignedUrlWithRoleARN (String endpoint, String bucketName, String objectName, Long expTimeDuration, String kmsCmkId, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "expTimeDuration", type = "long", desc = "URL Expiration Duration (minutes)" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsCmkId", type = "String", desc = "KMS Custom ID or null" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "Pre-signed URL" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Generate Pre-signed URL" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String url = S3.generatePreSignedUrlWithRoleARN(null,\"my-bucket\",\"my-key\", \"eu-west-1\" ...);\r\n"
    )
    public static String generatePreSignedUrlWithRoleARN(String endpoint, String bucketName, String objectName, long expTimeDuration, String kmsCmkId, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName,  roleARN,  roleSessionName,  duration,  awsAccessKey,  awsSecretKey);
        String result = generatePreSignedUrl(client, bucketName, objectName, kmsCmkId, expTimeDuration);
        client.shutdown();
        return result;
    }

    //-------------------------------- SAML --------------------------------

    @BEFunction(
        name = "deleteBucketWithSAML",
        signature = "void deleteBucketWithSAML(String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Delete a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "S3.deleteBucketWithSAML(null,\"my-bucket\", \"eu-west-1\" ...);\n"
    )
    public static void deleteBucketWithSAML(String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {


        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        deleteBucket(client, bucketName);
        client.shutdown();

    }

    @BEFunction(
        name = "createBucketWithSAML",
        signature = "String createBucketWithSAML (String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Create a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String name = S3.createBucketWithSAML(null,\"my-bucket\", \"eu-west-1\" ...);\n"
    )
    public static String createBucketWithSAML(String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        String name = createBucket(client, bucketName);
        client.shutdown();
        return name;
    }

    @BEFunction(
        name = "doesBucketExistWithSAML",
        signature = "boolean doesBucketExistWithSAML (String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Does a S3 Bucket exist" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesBucketExistWithSAML(null,\"my-bucket\", \"eu-west-1\" ...);\n"
    )
    public static boolean doesBucketExistWithSAML(String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        boolean exists = doesBucketExist(client, bucketName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "doesObjectExistWithSAML",
        signature = "boolean doesObjectExistWithSAML (String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Does Object exist in S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "boolean exists = S3.doesObjectExist(null,\"my-bucket\",\"my-object\",\"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static boolean doesObjectExistWithSAML(String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        boolean exists =  doesObjectExist(client, bucketName, objectName);
        client.shutdown();
        return exists;
    }

    @BEFunction(
        name = "deleteS3ObjectWithSAML",
        signature = "void deleteS3ObjectWithSAML (String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Delete an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectWithSAML(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static void deleteS3ObjectWithSAML(String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        deleteObject(client, bucketName, objectName);
        client.shutdown();

    }

    @BEFunction(
        name = "putS3ObjectWithSAML",
        signature = "String putS3ObjectWithSAML (String endpoint, String bucketName, String objectName, String objectContent, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectWithSAML(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectWithSAML(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        String result = putObject(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;

    }

    @BEFunction(
        name = "putS3ObjectUsingSSEKMSWithSAML",
        signature = "String putS3ObjectUsingSSES3ithSAML(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String result = S3.putS3ObjectUsingSSEKMSWithSAML(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSES3ithSAML(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        String result = putObjectWithSSE_S3(client, bucketName, objectName,  objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "putS3ObjectUsingSSEKMSWithSAML",
        signature = "String putS3ObjectUsingSSEKMSWithSAML(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsKeyID", type = "String", desc = "KMS Key ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingSSEKMSWithSAML(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingSSEKMSWithSAML(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        String result = putObjectWithSSE_KMS(client, bucketName, objectName, kmsKeyID, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "putS3ObjectUsingCSEKMSWithSAML",
        signature = "String putS3ObjectUsingCSEKMSWithSAML(String endpoint, String bucketName, String objectName, String objectContent,  String kmsKeyID, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectContent", type = "String", desc = "S3 Object Content" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsKeyID", type = "String", desc = "KMS Key ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Put an object to a S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultPut = S3.putS3ObjectUsingCSEKMSWithSAML(null,\"my-bucket\",\"data.xml\", \"hello, world\", \"eu-west-1\" ...);\n"
    )
    public static String putS3ObjectUsingCSEKMSWithSAML(String endpoint, String bucketName, String objectName, String objectContent,  String kmsKeyID, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3EncryptionV2 client = createS3Encryption( endpoint, regionName, kmsKeyID, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        String result = putObjectWithCSE_KMS(client, bucketName, objectName, objectContent);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "getS3ObjectWithSAML",
        signature = "String getS3ObjectWithSAML (String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Get an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String resultGet = S3.getS3ObjectWithSAML(null,\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static String getS3ObjectWithSAML(String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        String result = getObject(client, bucketName, objectName);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "getS3Object2WithSAML",
        signature = "String getS3Object2WithSAML (String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Get an object from S3 Bucket" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "Object obj = S3.getS3ObjectWithSAML(null,\"my-bucket\",\"data.xml\", \"eu-west-1\", \"...\", \"...\");\r\n"
    )
    public static Object getS3Object2WithSAML(String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
        Object result = getObject2(client, bucketName, objectName);
        client.shutdown();
        return result;
    }

    @BEFunction(
        name = "generatePreSignedUrlWithSAML",
        signature = "String generatePreSignedUrlWithSAML (String endpoint, String bucketName, String objectName, Long expTimeDuration, String kmsCmkId, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS S3 Service endpoint, set to null to use default S3 Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "bucketName", type = "String", desc = "S3 Bucket Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "objectName", type = "String", desc = "S3 Object Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "expTimeDuration", type = "long", desc = "URL Expiration Duration (minutes)" /*Add Description here*/),
            @FunctionParamDescriptor(name = "kmsCmkId", type = "String", desc = "KMS Custom ID or null" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "Pre-signed URL" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Generate Pre-signed URL" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String url = S3.generatePreSignedUrlWithSAML(null,\"my-bucket\",\"my-key\", \"eu-west-1\" ...);\r\n"
    )
    public static String generatePreSignedUrlWithSAML(String endpoint, String bucketName, String objectName, long expTimeDuration, String kmsCmkId, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        AmazonS3 client = createAmazonS3Client(endpoint, regionName,  idpName,  idpEntryUrl,  idpUsername,  idpPassword,  awsRole,  duration);
        String result = generatePreSignedUrl(client, bucketName, objectName, kmsCmkId, expTimeDuration);
        client.shutdown();
        return result;

    }

    //--------------------------------

    public static void deleteBucket(AmazonS3 client, String bucketName) throws RuntimeException {

        try {
            client.deleteBucket(bucketName);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String createBucket(AmazonS3 client, String bucketName) throws RuntimeException {

        try {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            com.amazonaws.services.s3.model.Bucket bucket = client.createBucket(createBucketRequest);
            return bucket.getName();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean doesBucketExist(AmazonS3 client, String bucketName) throws RuntimeException {

        try {
            return client.doesBucketExistV2(bucketName);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean doesObjectExist(AmazonS3 client, String bucketName, String objectName) throws RuntimeException {

        try {
            return client.doesObjectExist(bucketName, objectName);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObject(AmazonS3 client, String bucketName, String objectName, String content) throws RuntimeException {

        try {
            byte[] contentBytes = content.getBytes(StringUtils.UTF8);
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("text/plain");
            metadata.setContentLength(contentBytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObject(AmazonS3 client, String bucketName, String objectName, Object content) throws RuntimeException {

        try {
            byte[] contentBytes = SerializationUtils.serialize((Serializable) content);
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/octet-stream");
            metadata.setContentLength(contentBytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObjectWithCSE_KMS(AmazonS3EncryptionV2 client, String bucketName, String objectName, String content) throws RuntimeException {
        try {
            byte[] contentBytes = content.getBytes();
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("text/plain");
            metadata.setContentLength(contentBytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObjectWithCSE_KMS(AmazonS3EncryptionV2 client, String bucketName, String objectName, Object content) throws RuntimeException {
        try {
            byte[] contentBytes = SerializationUtils.serialize((Serializable) content);
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/octet-stream");
            metadata.setContentLength(contentBytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObjectWithSSE_KMS(AmazonS3 client, String bucketName, String objectName, String kmsKeyID, String content) throws RuntimeException {

        try {
            byte[] contentBytes = content.getBytes();
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentBytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata)
                .withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(kmsKeyID));

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObjectWithSSE_KMS(AmazonS3 client, String bucketName, String objectName, String kmsKeyID, Object content) throws RuntimeException {

        try {
            byte[] contentBytes = SerializationUtils.serialize((Serializable) content);
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentBytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata)
                .withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(kmsKeyID));

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObjectWithSSE_S3(AmazonS3 client, String bucketName, String objectName, String content) throws RuntimeException {
        
        try {
            byte[] contentBytes = content.getBytes();
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentBytes.length);
            metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String putObjectWithSSE_S3(AmazonS3 client, String bucketName, String objectName, Object content) throws RuntimeException {

        try {
            byte[] contentBytes = SerializationUtils.serialize((Serializable) content);
            InputStream is = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentBytes.length);
            metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is, metadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            return putObjectResult.getETag();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getObject(AmazonS3 client, String bucketName, String objectName) throws RuntimeException {

        try {
            S3Object s3Object = client.getObject(bucketName, objectName);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

            String text = IOUtils.toString(s3ObjectInputStream, StandardCharsets.UTF_8.name());

            return text;

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Object getObject2(AmazonS3 client, String bucketName, String objectName) throws RuntimeException {

        try {
            S3Object s3Object = client.getObject(bucketName, objectName);
            ObjectInputStream ois = new ObjectInputStream(s3Object.getObjectContent());
            Object object = ois.readObject();

            return object;

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteObject(AmazonS3 client, String bucketName, String objectName) throws RuntimeException {

        try {
            client.deleteObject(bucketName, objectName);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generatePreSignedUrl(AmazonS3 client, String bucketName, String key, String kmsCmkId, long expTimeDuration) throws RuntimeException {

        try {

            GeneratePresignedUrlRequest generatePresignedUrlRequest;

            java.util.Date expiration = new java.util.Date();
            // Dealing with milliseconds and expTimeDuration is in minutes
            expiration.setTime(expiration.getTime() + expTimeDuration * 60 * 1000);
          if (kmsCmkId != null && kmsCmkId.length() > 0) {
             generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.GET)
                    .withKmsCmkId(kmsCmkId)
                    .withExpiration(expiration);
                } else {
               generatePresignedUrlRequest =
                  new GeneratePresignedUrlRequest(bucketName, key)
                      .withMethod(HttpMethod.GET)
                      .withExpiration(expiration);
          }

            URL url = client.generatePresignedUrl(generatePresignedUrlRequest);

            return url.toString();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static AmazonS3 createAmazonS3Client(String endpoint, String regionName) throws RuntimeException {


        if (endpoint != null && endpoint.length() != 0) {


            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);


            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .standard()
                .withEndpointConfiguration(config)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

            return client;

        } else {
            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .standard()
                .withRegion(regionName)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

            return client;
        }

    }

    public static AmazonS3 createAmazonS3Client(String endpoint, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        if (endpoint != null && endpoint.length() != 0) {


            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);


            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();

            return client;

        } else {
            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .standard()
                .withRegion(regionName)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();

            return client;
        }

    }

    public static AmazonS3 createAmazonS3Client(String endpoint, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        Credentials creds = StsUtils
            .createCredentialsWithAssumeRole(roleARN, roleSessionName, regionName, duration, awsAccessKey, awsSecretKey);

        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                creds.getAccessKeyId(),
                creds.getSecretAccessKey(),
                creds.getSessionToken());

        if (endpoint != null && endpoint.length() != 0) {

            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .build();

            return client;

        } else {
            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withRegion(regionName)
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .build();

            return client;
        }
    }

    public static AmazonS3 createAmazonS3Client(String endpoint, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        Credentials credentials = null;
        try {
            credentials = createCredentialsWithSAML(idpName,  idpEntryUrl,  idpUsername,  idpPassword, regionName, awsRole, duration, false, null ,  null,  null,  0);


        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
            credentials.getAccessKeyId(),
            credentials.getSecretAccessKey(),
            credentials.getSessionToken());

        if (endpoint!=null && endpoint.length() != 0) {

            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .build();

            return client;


        } else {
            AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withRegion(regionName)
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .build();

           return client;
        }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
   }

    public static AmazonS3EncryptionV2 createS3Encryption(String endpoint, String regionName, String kmsKeyID) throws RuntimeException {

        if (endpoint != null && endpoint.length() != 0){

            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withEndpointConfiguration(config)
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
                .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

            return s3Encryption;

        } else {
            AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(regionName)
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
                .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

            return s3Encryption;
        }

    }

    public static AmazonS3EncryptionV2 createS3Encryption(String endpoint, String regionName, String kmsKeyID, String awsAccessKey, String awsSecretKey) throws RuntimeException {

       BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

       if (endpoint != null && endpoint.length() != 0){

           AwsClientBuilder.EndpointConfiguration config =
               new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

           AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
               .withEndpointConfiguration(config)
               .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
               .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
               .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
               .build();

           return s3Encryption;

       } else {
       AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
           .withRegion(regionName)
           .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
           .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
           .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
           .build();

       return s3Encryption;
       }

   }

    public static AmazonS3EncryptionV2 createS3Encryption(String endpoint, String regionName, String kmsKeyID, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
        throws RuntimeException {


            Credentials credentials = null;
        try {
            credentials = createCredentialsWithSAML(idpName,  idpEntryUrl,  idpUsername,  idpPassword, regionName, awsRole, duration, false, null ,  null,  null,  0);

            BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                credentials.getAccessKeyId(),
                credentials.getSecretAccessKey(),
                credentials.getSessionToken());

            if (endpoint != null && endpoint.length() != 0){

                AwsClientBuilder.EndpointConfiguration config =
                    new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

                AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                    .withEndpointConfiguration(config)
                    .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
                    .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
                    .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                    .build();

                return s3Encryption;

            } else {
                AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                    .withRegion(regionName)
                    .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
                    .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
                    .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                    .build();

                return s3Encryption;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static AmazonS3EncryptionV2 createS3Encryption(String endpoint, String regionName, String kmsKeyID, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        Credentials creds = StsUtils
            .createCredentialsWithAssumeRole(roleARN, roleSessionName, regionName, duration, awsAccessKey, awsSecretKey);

        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
            creds.getAccessKeyId(),
            creds.getSecretAccessKey(),
            creds.getSessionToken());


        if (endpoint != null && endpoint.length() != 0) {

            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withEndpointConfiguration(config)
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
                .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .build();
            return s3Encryption;
        } else {
            AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(regionName)
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode((CryptoMode.StrictAuthenticatedEncryption)))
                .withEncryptionMaterialsProvider( new KMSEncryptionMaterialsProvider(kmsKeyID))
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .build();
            return s3Encryption;
        }




    }

}
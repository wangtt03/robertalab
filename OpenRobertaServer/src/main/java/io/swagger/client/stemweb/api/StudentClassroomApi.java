/*
 * stemgarden api
 * stemgarden api
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.stemweb.api;

import io.swagger.client.stemweb.ApiCallback;
import io.swagger.client.stemweb.ApiClient;
import io.swagger.client.stemweb.ApiException;
import io.swagger.client.stemweb.ApiResponse;
import io.swagger.client.stemweb.Configuration;
import io.swagger.client.stemweb.Pair;
import io.swagger.client.stemweb.ProgressRequestBody;
import io.swagger.client.stemweb.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.stemweb.model.Classroom;
import java.util.UUID;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentClassroomApi {
    private ApiClient apiClient;

    public StudentClassroomApi() {
        this(Configuration.getDefaultApiClient());
    }

    public StudentClassroomApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getStudentClassroomsById
     * @param userId id of the student (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getStudentClassroomsByIdCall(UUID userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/studentClassroom/{userId}"
            .replaceAll("\\{" + "userId" + "\\}", apiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getStudentClassroomsByIdValidateBeforeCall(UUID userId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getStudentClassroomsById(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getStudentClassroomsByIdCall(userId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * get classroom students
     * get classroom students
     * @param userId id of the student (required)
     * @return List&lt;Classroom&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<Classroom> getStudentClassroomsById(UUID userId) throws ApiException {
        ApiResponse<List<Classroom>> resp = getStudentClassroomsByIdWithHttpInfo(userId);
        return resp.getData();
    }

    /**
     * get classroom students
     * get classroom students
     * @param userId id of the student (required)
     * @return ApiResponse&lt;List&lt;Classroom&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<Classroom>> getStudentClassroomsByIdWithHttpInfo(UUID userId) throws ApiException {
        com.squareup.okhttp.Call call = getStudentClassroomsByIdValidateBeforeCall(userId, null, null);
        Type localVarReturnType = new TypeToken<List<Classroom>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * get classroom students (asynchronously)
     * get classroom students
     * @param userId id of the student (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getStudentClassroomsByIdAsync(UUID userId, final ApiCallback<List<Classroom>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getStudentClassroomsByIdValidateBeforeCall(userId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<Classroom>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}

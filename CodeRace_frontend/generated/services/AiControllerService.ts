/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_string_ } from '../models/BaseResponse_string_';
import type { ProblemAnalysisRequest } from '../models/ProblemAnalysisRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class AiControllerService {
    /**
     * analysis
     * @param problemAnalysisRequest problemAnalysisRequest
     * @returns BaseResponse_string_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static analysisUsingPost(
        problemAnalysisRequest: ProblemAnalysisRequest,
    ): CancelablePromise<BaseResponse_string_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/AiChat/analysis',
            body: problemAnalysisRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * answerAnalysis
     * @param answer answer
     * @param content content
     * @returns string OK
     * @throws ApiError
     */
    public static answerAnalysisUsingGet(
        answer?: string,
        content?: string,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/AiChat/answerAnalysis',
            query: {
                'answer': answer,
                'content': content,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * doChat
     * @param message message
     * @returns string OK
     * @throws ApiError
     */
    public static doChatUsingGet(
        message?: string,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/AiChat/chat',
            query: {
                'message': message,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * problemAnalysis
     * @param content content
     * @returns string OK
     * @throws ApiError
     */
    public static problemAnalysisUsingGet(
        content?: string,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/AiChat/problemAnalysis',
            query: {
                'content': content,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
}

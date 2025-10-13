// Centralized error handling utility
export interface ApiError {
	message: string;
	status?: number;
	code?: string;
	details?: any;
}

export class AppError extends Error {
	public status?: number;
	public code?: string;
	public details?: any;

	constructor(
		message: string,
		status?: number,
		code?: string,
		details?: any
	) {
		super(message);
		this.name = 'AppError';
		this.status = status;
		this.code = code;
		this.details = details;
	}
}

// Error message mapping for user-friendly messages
const ERROR_MESSAGES = {
	// Network errors
	NETWORK_ERROR: 'Network error. Please check your connection and try again.',
	TIMEOUT_ERROR: 'Request timed out. Please try again.',

	// Authentication errors
	UNAUTHORIZED: 'Authentication failed. Please login again.',
	FORBIDDEN:
		'Access denied. You do not have permission to perform this action.',

	// Validation errors
	VALIDATION_ERROR: 'Please check your input and try again.',
	INVALID_EMAIL: 'Please enter a valid email address.',
	INVALID_PASSWORD: 'Password must be at least 8 characters long.',

	// Server errors
	SERVER_ERROR: 'Internal server error. Please try again later.',
	SERVICE_UNAVAILABLE:
		'Service temporarily unavailable. Please try again later.',

	// Rate limiting
	RATE_LIMIT: 'Too many requests. Please try again later.',

	// Generic errors
	UNKNOWN_ERROR: 'An unexpected error occurred. Please try again.',
	NOT_FOUND: 'Resource not found.',
	CONFLICT: 'A resource with this information already exists.',
};

// Error handler function
export function handleApiError(error: any): ApiError {
	console.error('[ErrorHandler] Error:', error);

	// If it's already an AppError, return it
	if (error instanceof AppError) {
		return {
			message: error.message,
			status: error.status,
			code: error.code,
			details: error.details,
		};
	}

	// Handle Axios errors
	if (error.response) {
		const status = error.response.status;
		const data = error.response.data;

		switch (status) {
			case 400:
				return {
					message: data?.message || ERROR_MESSAGES.VALIDATION_ERROR,
					status,
					code: 'BAD_REQUEST',
					details: data,
				};
			case 401:
				return {
					message: ERROR_MESSAGES.UNAUTHORIZED,
					status,
					code: 'UNAUTHORIZED',
					details: data,
				};
			case 403:
				return {
					message: ERROR_MESSAGES.FORBIDDEN,
					status,
					code: 'FORBIDDEN',
					details: data,
				};
			case 404:
				return {
					message: ERROR_MESSAGES.NOT_FOUND,
					status,
					code: 'NOT_FOUND',
					details: data,
				};
			case 409:
				return {
					message:
						data?.message || 'Conflict: Resource already exists',
					status,
					code: 'CONFLICT',
					details: data,
				};
			case 422:
				return {
					message: data?.message || ERROR_MESSAGES.VALIDATION_ERROR,
					status,
					code: 'VALIDATION_ERROR',
					details: data,
				};
			case 429:
				return {
					message: ERROR_MESSAGES.RATE_LIMIT,
					status,
					code: 'RATE_LIMIT',
					details: data,
				};
			case 500:
				return {
					message: ERROR_MESSAGES.SERVER_ERROR,
					status,
					code: 'SERVER_ERROR',
					details: data,
				};
			case 503:
				return {
					message: ERROR_MESSAGES.SERVICE_UNAVAILABLE,
					status,
					code: 'SERVICE_UNAVAILABLE',
					details: data,
				};
			default:
				return {
					message: data?.message || `HTTP Error ${status}`,
					status,
					code: 'HTTP_ERROR',
					details: data,
				};
		}
	}

	// Handle network errors
	if (error.request) {
		return {
			message: ERROR_MESSAGES.NETWORK_ERROR,
			code: 'NETWORK_ERROR',
			details: error.request,
		};
	}

	// Handle timeout errors
	if (error.code === 'ECONNABORTED') {
		return {
			message: ERROR_MESSAGES.TIMEOUT_ERROR,
			code: 'TIMEOUT_ERROR',
			details: error,
		};
	}

	// Handle other errors
	return {
		message: error.message || ERROR_MESSAGES.UNKNOWN_ERROR,
		code: 'UNKNOWN_ERROR',
		details: error,
	};
}

// Utility function to create user-friendly error messages
export function getUserFriendlyMessage(error: ApiError): string {
	// If the error already has a user-friendly message, return it
	if (error.message && !error.message.includes('HTTP Error')) {
		return error.message;
	}

	// Map error codes to user-friendly messages
	switch (error.code) {
		case 'NETWORK_ERROR':
			return ERROR_MESSAGES.NETWORK_ERROR;
		case 'TIMEOUT_ERROR':
			return ERROR_MESSAGES.TIMEOUT_ERROR;
		case 'UNAUTHORIZED':
			return ERROR_MESSAGES.UNAUTHORIZED;
		case 'FORBIDDEN':
			return ERROR_MESSAGES.FORBIDDEN;
		case 'VALIDATION_ERROR':
			return ERROR_MESSAGES.VALIDATION_ERROR;
		case 'SERVER_ERROR':
			return ERROR_MESSAGES.SERVER_ERROR;
		case 'SERVICE_UNAVAILABLE':
			return ERROR_MESSAGES.SERVICE_UNAVAILABLE;
		case 'RATE_LIMIT':
			return ERROR_MESSAGES.RATE_LIMIT;
		case 'NOT_FOUND':
			return ERROR_MESSAGES.NOT_FOUND;
		case 'CONFLICT':
			return ERROR_MESSAGES.CONFLICT;
		default:
			return ERROR_MESSAGES.UNKNOWN_ERROR;
	}
}

// Utility function to check if error is retryable
export function isRetryableError(error: ApiError): boolean {
	const retryableStatuses = [408, 429, 500, 502, 503, 504];
	const retryableCodes = [
		'NETWORK_ERROR',
		'TIMEOUT_ERROR',
		'RATE_LIMIT',
		'SERVER_ERROR',
		'SERVICE_UNAVAILABLE',
	];

	return (
		retryableStatuses.includes(error.status || 0) ||
		retryableCodes.includes(error.code || '')
	);
}

// Utility function to log errors for debugging
export function logError(error: any, context?: string): void {
	const timestamp = new Date().toISOString();
	const errorInfo = {
		timestamp,
		context,
		error:
			error instanceof Error
				? {
						name: error.name,
						message: error.message,
						stack: error.stack,
					}
				: error,
	};

	console.error('[ErrorLogger]', errorInfo);

	// In production, you might want to send this to an error tracking service
	// like Sentry, LogRocket, etc.
	if (process.env.NODE_ENV === 'production') {
		// Example: sendToErrorTrackingService(errorInfo);
	}
}

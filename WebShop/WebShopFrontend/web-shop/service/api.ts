import axios, {
	AxiosInstance,
	AxiosRequestConfig,
	AxiosResponse,
	AxiosError,
} from 'axios';
import { handleApiError, logError } from './errorHandler';
import { authService } from './authService';

// Get the backend URL from environment or use default
const getBackendUrl = () => {
	const envUrl = process.env.NEXT_PUBLIC_BACKEND_URL;
	if (envUrl) {
		console.log('[API] Using backend URL from environment:', envUrl);
		return envUrl;
	}

	// Default to localhost:3000 if no environment variable is set
	const defaultUrl = 'http://localhost:3000';
	console.warn(
		'[API] No NEXT_PUBLIC_BACKEND_URL found, using default:',
		defaultUrl
	);
	return defaultUrl;
};

// Create axios instance with default config
const axiosInstance: AxiosInstance = axios.create({
	baseURL: getBackendUrl(),
	timeout: 10000, // 10 seconds timeout
	withCredentials: true, // Enable sending cookies with requests
	headers: {
		'Content-Type': 'application/json',
	},
});

// Request interceptor to add auth token
axiosInstance.interceptors.request.use(
	config => {
		// Log the request for debugging
		console.log('[API] Request:', {
			method: config.method?.toUpperCase(),
			url: config.url,
			baseURL: config.baseURL,
			fullURL: `${config.baseURL}${config.url}`,
		});

		// Get token from localStorage if available (client-side only)
		if (typeof window !== 'undefined') {
			const token = localStorage.getItem('access_token');
			if (token) {
				config.headers.Authorization = `Bearer ${token}`;
				console.log('[API] Added Authorization header');
				console.log('[API] Token:', token);
				console.log(config.headers.Authorization);
			}
		}
		return config;
	},
	error => {
		logError(error, 'Request interceptor');
		return Promise.reject(error);
	}
);

// Response interceptor for error handling
axiosInstance.interceptors.response.use(
	(response: AxiosResponse) => {
		console.log('[Axios] Response:', response.data);
		return response;
	},
	async (error: AxiosError) => {
		// Use centralized error handler
		const apiError = handleApiError(error);

		// Handle authentication errors specifically
		if (apiError.status === 401) {
                console.log('[API] 401 Unauthorized - attempting token refresh...');
                console.log('[API] Token expired - logging out user');
                authService.logout();
		}

		// Log error for debugging
		logError(error, 'Response interceptor');

		// Throw the processed error
		throw new Error(apiError.message);
	}
);

// Enhanced API utility function
export async function apiRequest<T = any>(
	config: AxiosRequestConfig,
	customHeaders: Record<string, string> = {}
): Promise<T> {
	try {
		// Merge custom headers
		const finalConfig: AxiosRequestConfig = {
			...config,
			headers: {
				...config.headers,
				...customHeaders,
			},
		};

		const response = await axiosInstance(finalConfig);
		return response.data;
	} catch (error) {
		// Re-throw the error (already handled by interceptor)
		throw error;
	}
}

// Convenience methods for common HTTP operations
export const apiService = {
	get: <T = any>(url: string, config?: AxiosRequestConfig) =>
		apiRequest<T>({ ...config, method: 'GET', url }),

	post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) =>
		apiRequest<T>({ ...config, method: 'POST', url, data }),

	put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) =>
		apiRequest<T>({ ...config, method: 'PUT', url, data }),

	patch: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) =>
		apiRequest<T>({ ...config, method: 'PATCH', url, data }),

	delete: <T = any>(url: string, config?: AxiosRequestConfig) =>
		apiRequest<T>({ ...config, method: 'DELETE', url }),
};

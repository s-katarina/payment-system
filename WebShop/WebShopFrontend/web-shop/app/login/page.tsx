'use client';
import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { authService } from '@/service/authService';
 
export default function LoginPage() {

  const router = useRouter()
  const [error, setError] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
 
  const validateEmail = (email: string) => {
		return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
	};

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		if (!validateEmail(email)) {
			setError('Please enter a proper email');
			return;
		}
		setError('');
		setIsLoading(true);
		try {
			const data = await authService.login(email, password);
		if (data && data.accessToken) {
			localStorage.setItem('access_token', data.accessToken);
		}
	} catch (error) {
		setError('Login failed. Please try again.');
	} finally {
		setIsLoading(false);
	}
}
 
  return (
    <form onSubmit={handleSubmit} className="form">
      <div className="form-container">
        <h2 className="form-title">Login</h2>
        <input type="email" className="form-input" placeholder="Email" />
        <input type="password" className="form-input" placeholder="Password" />
        <button type="submit" disabled={isLoading} className="form-button">
          {isLoading ? 'Logging in...' : 'Login'}
        </button>
      </div>
    </form>

  )
}
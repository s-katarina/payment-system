'use client';
import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { authService } from '@/service/authService';
import { useAuth } from '@/app/AuthContext';
import toast from 'react-hot-toast';
 
export default function LoginPage() {

  const router = useRouter()
  const { setUser } = useAuth();
  const [error, setError] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    toast.error(error);
  }, [error]);
 
  const validateEmail = (email: string) => {
		return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
	};

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		if (!validateEmail(email)) {
			setError('Please enter a proper email');
          toast.error('Registration failed. Please try again.');

			return;
		}
		setIsLoading(true);
		try {
			const data = await authService.login(email, password);
      if (data && data.accessToken) {
        // Update AuthContext with the logged-in user
        setUser(data.userDTO);
        router.replace('/');
        toast.success('Login successful!');
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
        <input
          id='email'
          type="email"
          className="form-input"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        <input
          id='password'
          type="password"
          className="form-input"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />        
        <button type="submit" disabled={isLoading} className="form-button">
          {isLoading ? 'Logging in...' : 'Login'}
        </button>
      </div>
    </form>

  )
}
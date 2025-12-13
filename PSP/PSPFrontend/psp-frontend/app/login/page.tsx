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
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (error) {
      toast.error(error);
    }
  }, [error]);
 
  const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		if (!username || !password) {
			setError('Please enter both username and password');
      toast.error('Please enter both username and password');
			return;
		}
		setIsLoading(true);
		try {
			const data = await authService.login(username, password);
      if (data && data.accessToken) {
        // Update AuthContext with the logged-in user
        setUser(data.userDTO);
        router.replace('/merchants');
        toast.success('Login successful!');
      }
    } catch (error: any) {
      setError(error.message || 'Login failed. Please try again.');
      toast.error(error.message || 'Login failed. Please try again.');
    } finally {
      setIsLoading(false);
    }
}
 
  return (
    <form onSubmit={handleSubmit} className="form">
      <div className="form-container">
        <h2 className="form-title">PSP Admin Login</h2>
        <input
          id='username'
          type="text"
          className="form-input"
          placeholder="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
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


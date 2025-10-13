import { useState } from 'react';
import { useRouter } from 'next/router';
import { authService } from '@/service/authService';
import { toast, Toaster } from 'react-hot-toast';

export default function RegisterPage() {
  const router = useRouter();
  const [username, setUsername] = useState('');
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const validateEmail = (email: string) => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Validation
    if (!username || !name || !password) {
      toast.error('All fields are required');
      return;
    }
    if (!validateEmail(username)) {
      toast.error('Please enter a valid email address');
      return;
    }
    if (name.length > 50) {
      toast.error('Name cannot be longer than 50 characters');
      return;
    }
    if (password.length < 8 || password.length > 15) {
      toast.error('Password must be between 8 and 15 characters');
      return;
    }

    setIsLoading(true);
    try {
      await authService.register(username, name, password);
      toast.success('Registration successful!');
      router.push('/login');
    } catch (err) {
      toast.error('Registration failed. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <Toaster position="top-right" />
      <h2>Register</h2>
      <input
        type="email"
        name="username"
        placeholder="Email"
        required
        value={username}
        onChange={e => setUsername(e.target.value)}
      />
      <input
        type="text"
        name="name"
        placeholder="Name"
        required
        value={name}
        onChange={e => setName(e.target.value)}
        maxLength={50}
      />
      <input
        type="password"
        name="password"
        placeholder="Password"
        required
        value={password}
        onChange={e => setPassword(e.target.value)}
        minLength={8}
        maxLength={15}
      />
      <button type="submit" disabled={isLoading}>Register</button>
    </form>
  );
}

'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Label from '@/components/Label';
import TextField from '@/components/TextField';
import Password from '@/components/Password';

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const router = useRouter();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/login', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ username, password }),
                        });

            if (response.ok) {
                const { accessToken } = await response.json();
                localStorage.setItem('access_token', accessToken);
                console.log('Login successful');
                router.push('/expenseBook');
            } else {
                // TODO 失敗したら、エラーメッセージなどをログイン画面に表示させる
                console.log('Login failed');
            }
        } catch (err) {
            console.log(`Login Error`);
        }


    }

    return (
        <div className="min-h-screen bg-gray-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-md">
                <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    Login
                </h2>
            </div>
            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
                <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
                    <form className="space-y-6"
                        onSubmit={handleSubmit}
                    >
                        <div>
                            <Label htmlFor="username">Username:</Label>
                            <div className="mt-1">
                                <TextField required id="username" value={username} onChange={(e) => setUsername(e.target.value)} />
                            </div>
                        </div>
                        <div>
                            <Label htmlFor="password">Password:</Label>
                            <div className="mt-1">
                                <Password id="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                            </div>
                        </div>
                        <div>
                            <button type="submit"
                                    className="{`
                                        w-full
                                        flex
                                        justify-center
                                        py-2 px-4
                                        border border-transparent
                                        rounded-md
                                        shadow-sm
                                        text-sm
                                        font-medium
                                        text-white
                                        bg-indigo-600
                                        hover:bg-indigo-700
                                        focus:outline-none
                                        focus:ring-2
                                        focus:ring-offset-2
                                        focus:ring-indigo-500
                                    `}">
                                Login
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

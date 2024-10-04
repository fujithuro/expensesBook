'use client';

import React, { useState } from 'react';
import Label from '@/components/Label';

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('/api/login', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ username, password }),
                        });

            if (response.ok) {
                console.log('Login successful');
            } else {
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
                                <input type="text" id="username" name="username" required
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    className="{`
                                        appearance-none
                                        block
                                        w-full
                                        px-3 py-2
                                        border border-gray-300
                                        rounded-md
                                        shadow-sm
                                        text-gray-800
                                        placeholder-gray-400
                                        focus:outline-none
                                        focus:ring-indigo-500
                                        focus:border-indigo-500
                                        sm:text-sm
                                    `}"
                                />
                            </div>
                        </div>
                        <div>
                            <Label htmlFor="password">Password:</Label>
                            <div className="mt-1">
                                <input type="password" id="password" name="password" required
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="{`
                                        appearance-none
                                        block
                                        w-full
                                        px-3 py-2
                                        border border-gray-300
                                        rounded-md
                                        shadow-sm
                                        text-gray-800
                                        placeholder-gray-400
                                        focus:outline-none
                                        focus:ring-indigo-500
                                        focus:border-indigo-500
                                        sm:text-sm
                                    `}"/>
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

'use client';

import React from 'react';

const Password = ({ id, value, onChange, required = true }) => (
    <input type="password"
            id={id}
            name={value}
            required={required}
            value={value}
            onChange={onChange}
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
);

export default Password;

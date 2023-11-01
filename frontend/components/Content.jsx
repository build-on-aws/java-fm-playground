"use client";

export default function Content({children}) {
    return (
        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-200">
            {children}
        </main>
    )
}
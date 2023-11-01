"use client";

import React from "react";

export default async function Models() {
    const res = await fetch("http://localhost:8080/foundation-models");
    const data = await res.json();

    return (
        <div className="container px-6 py-8 mx-auto">
            <h3 className="text-3xl font-medium text-gray-700">Foundation Models</h3>
            <div className="flex flex-col mt-8">
                <div className="py-2 -my-2 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
                    <div
                        className="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
                        <table className="min-w-full">
                            <thead>
                            <tr>
                                <th className="px-6 py-4 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                                    Provider
                                </th>
                                <th className="px-6 py-4 text-xs font-medium leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                                    Name
                                </th>
                                <th className="px-6 py-4 border-b border-gray-200 bg-gray-50"></th>
                            </tr>
                            </thead>
                            <tbody className="bg-white">
                            {data.map(item => (

                                <tr key={item.modelId}>
                                    <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                        {item.providerName}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                        {item.modelName}
                                    </td>
                                    <td className="px-6 py-4 text-sm font-medium leading-5 text-right whitespace-no-wrap border-b border-gray-200">
                                        <a href={`/models/${item.modelId}`}
                                           className="text-indigo-600 hover:text-indigo-900">Show details</a>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    )
}

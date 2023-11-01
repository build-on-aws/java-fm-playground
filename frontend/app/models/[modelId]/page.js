import React from "react";
import Link from "next/link";

export default async function Model({params: {modelId}}) {
    const res = await fetch(`http://localhost:8080/foundation-models/model?id=${modelId}`);
    const data = await res.json();

    return (
        <div className="container px-6 py-8 mx-auto">
            <h3 className="text-3xl font-medium text-gray-700">Foundation Model Details</h3>
            <div className="flex flex-col mt-8">
                <div className="py-2 -my-2 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
                    <div
                        className="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
                        <table className="min-w-full">
                            <tbody className="bg-white">
                            <tr>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    Provider:
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    {data.providerName}
                                </td>
                            </tr>
                            <tr>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    Name:
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    {data.modelName}
                                </td>
                            </tr>
                            <tr>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    Model ID:
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    {data.modelId}
                                </td>
                            </tr>
                            <tr>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    Customizations:
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    {data.customizationsSupported}
                                </td>
                            </tr>
                            <tr>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    Output Modalities:
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    {data.outputModalities}
                                </td>
                            </tr>
                            <tr>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    Model ARN:
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap border-b border-gray-200">
                                    {data.modelArn}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    )
}
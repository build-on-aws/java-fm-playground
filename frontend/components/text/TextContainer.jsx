"use client";

import Human from "@/components/chat/Human";
import React, { useState } from "react";
import Assistant from "@/components/chat/Assistant";
import Loader from "@/components/chat/Loader";

export default function TextContainer() {
    // const [conversation, setConversation] = useState([]);
    // const [inputValue, setInputValue] = useState("");
    // const [isLoading, setIsLoading] = useState(false);
    //
    // const handleInputChange = (e) => {
    //     setInputValue(e.target.value);
    // };
    //
    // const extractPrompt = (body) => {
    //     let conversationBuilder = '';
    //     for (const message of body) {
    //         conversationBuilder += `${message.sender}: ${message.message}\n\n`;
    //     }
    //
    //     return conversationBuilder.trim();
    // }
    //
    // const sendMessage = async () => {
    //     const newMessage = { sender: "Human", message: inputValue };
    //     setConversation(prevConversation => [...prevConversation, newMessage]);
    //     setInputValue('');
    //
    //     try {
    //         const prompt = extractPrompt([...conversation, newMessage]);
    //
    //         setIsLoading(true);
    //
    //         const response = await fetch("http://localhost:8080/foundation-models/model/text/anthropic.claude-v2/invoke", {
    //             method: 'POST',
    //             headers: { 'Content-Type': 'application/json' },
    //             body: JSON.stringify({prompt: prompt})
    //         });
    //
    //         if (!response.ok) {
    //             throw new Error(`HTTP error! status: ${response.status}`);
    //         }
    //
    //         await response.json().then(data => {
    //             setConversation(prevConversation => [...prevConversation, {
    //                 sender: "Assistant",
    //                 message: data.completion
    //             }]);
    //         });
    //
    //     } catch (error) {
    //         console.error("Error sending message:", error);
    //     } finally {
    //         setIsLoading(false);
    //     }
    // };

    return <div className="flex flex-col flex-auto h-full p-6">
        <h3 className="text-3xl font-medium text-gray-700">Chat Playground (Anthropic Claude V2)</h3>
        <div className="flex flex-col flex-auto flex-shrink-0 rounded-2xl bg-gray-100 p-4 mt-8">
            <div className="flex flex-col h-full overflow-x-auto mb-4">
                <div className="flex flex-col h-full">
                    <div className="mb-4 w-full bg-gray-50 rounded-lg border border-gray-200 dark:bg-gray-700 dark:border-gray-600">
                        <div className="p-4 bg-white rounded-xl dark:bg-gray-800">
                            <textarea id="input" rows="20"
                                      className="block px-0 w-full text-sm text-gray-800 bg-white border-0 dark:bg-gray-800 focus:ring-0 dark:text-white dark:placeholder-gray-400"
                                      placeholder="Write something..." required>
                            </textarea>
                        </div>
                    </div>
                    {/*<input*/}
                    {/*    value={inputValue}*/}
                    {/*    onChange={handleInputChange}*/}
                    <button
                        type="button"
                        // onClick={sendMessage}
                        className="flex w-[100px] items-center justify-center bg-indigo-500 hover:bg-indigo-600 rounded-xl text-white px-4 py-1 flex-shrink-0">
                        <span>Send</span>
                        <span className="ml-2">
                          <svg
                              className="w-4 h-4 transform rotate-45 -mt-px"
                              fill="none"
                              stroke="currentColor"
                              viewBox="0 0 24 24"
                              xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth="2"
                                d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"
                            ></path>
                          </svg>
                        </span>
                    </button>
                </div>
            </div>
        </div>
    </div>;
}
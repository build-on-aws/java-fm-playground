"use client";

import Human from "@/components/chat/Human";
import React, { useState } from "react";
import Assistant from "@/components/chat/Assistant";

export default function ChatContainer() {
    const [conversation, setConversation] = useState([]);
    const [inputValue, setInputValue] = useState("");

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    const extractPrompt = (body) => {
        let conversationBuilder = '';
        for (const message of body) {
            conversationBuilder += `${message.sender}: ${message.message}\n\n`;
        }

        return conversationBuilder.trim();
    }

    const sendMessage = async () => {
        const newMessage = { sender: "Human", message: inputValue };
        setConversation(prevConversation => [...prevConversation, newMessage]);
        setInputValue('');

        try {
            const prompt = extractPrompt([...conversation, newMessage]);
            const response = await fetch("http://localhost:8080/foundation-models/model/text/anthropic.claude-v2/invoke", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({prompt: prompt})
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            await response.json().then(data => {
                setConversation(prevConversation => [...prevConversation, {
                    sender: "Assistant",
                    message: data.completion
                }]);
            });

        } catch (error) {
            console.error("Error sending message:", error);
        }
    };

    return (
        <div className="flex flex-col flex-auto h-full p-6">
            <h3 className="text-3xl font-medium text-gray-700">Chat Playground (Anthropic Claude V2)</h3>
            <div className="flex flex-col flex-auto flex-shrink-0 rounded-2xl bg-gray-100 p-4 mt-8">
                <div className="flex flex-col h-full overflow-x-auto mb-4">
                    <div className="flex flex-col h-full">
                        <div className="grid grid-cols-12 gap-y-2">
                            {conversation.map((item, i) => (
                                item.sender === "Assistant" ? (
                                    <Assistant text={item.message} key={i}/>
                                ) : (
                                    <Human text={item.message} key={i}/>
                                )
                            ))}
                        </div>
                    </div>
                </div>
                <div className="flex flex-row items-center h-16 rounded-xl bg-white w-full px-4">
                    <div className="flex-grow">
                        <div className="relative w-full">
                            <input
                                type="text"
                                value={inputValue}
                                onChange={handleInputChange}
                                onKeyPress={(event) => {
                                    if (event.key === 'Enter') {
                                        sendMessage();
                                    }
                                }}
                                placeholder="Send a message"
                                className="flex w-full border rounded-xl focus:outline-none focus:border-indigo-300 pl-4 h-10"
                            />
                        </div>
                    </div>
                    <div className="ml-4">
                        <button
                            type="button"
                            onClick={sendMessage}
                            className="flex items-center justify-center bg-indigo-500 hover:bg-indigo-600 rounded-xl text-white px-4 py-1 flex-shrink-0"
                        >
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
        </div>
    );
}
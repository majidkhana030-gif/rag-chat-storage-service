package com.assignment.chatstorage.constant;

public final class SwaggerDocConstants {
    public static final String CREATE_MESSAGE_SUMMARY = "Add a new message to a chat session";
    public static final String CREATE_MESSAGE_DESCRIPTION = "Stores a message in the specified chat session, including sender type, content, and optional RAG context.";
    public static final String GET_MESSAGE_SUMMARY = "Get messages of a chat session";
    public static final String GET_MESSAGE_DESCRIPTION = "Retrieves paginated chat messages for the specified session ID.";
    public static final String DELETE_MESSAGE_SUMMARY = "Delete a message by ID";
    public static final String DELETE_MESSAGE_DESCRIPTION = "Deletes a specific chat message from a session by message ID.";

    public static final String CREATE_CHAT_SESSION_SUMMARY = "Create a new chat session";
    public static final String CREATE_CHAT_SESSION_DESCRIPTION  = "Starts a new chat session for a user. Returns the created session details.";

    public static final String GET_SESSION_BY_ID_SUMMARY = "Get a chat session by ID";
    public static final String GET_SESSION_BY_ID_DESCRIPTION  = "Retrieves a chat session using its unique ID.";

    public static final String GET_SESSION_BY_USER_SUMMARY = "Get all chat sessions of a user";
    public static final String GET_SESSION_BY_USER_DESCRIPTION  = "Returns all chat sessions for a specific user ID.";

    public static final String UPDATE_SESSION_SUMMARY = "Update a chat session";
    public static final String UPDATE_SESSION_DESCRIPTION  = "Allows renaming a session or marking it as favorite.";

    public static final String DELETE_SESSION_SUMMARY = "Delete a chat session";
    public static final String DELETE_SESSION_DESCRIPTION  = "Deletes a session along with all its associated messages.";

    private SwaggerDocConstants(){}
}

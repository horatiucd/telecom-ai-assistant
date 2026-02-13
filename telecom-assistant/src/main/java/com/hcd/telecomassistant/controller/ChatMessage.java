package com.hcd.telecomassistant.controller;

public record ChatMessage(Type type, String content) {

    public enum Type { USER, ASSISTANT }
}

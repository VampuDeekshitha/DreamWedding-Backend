package com.tietheknot.controller;

import com.tietheknot.entity.User;

/** Public user fields returned to the frontend. Never expose stored password hashes. */
public record UserResponse(Long id, String name, String email, String role) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}

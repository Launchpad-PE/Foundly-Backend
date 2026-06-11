package com.foundly.foundlyplatform.iam.domain.model.queries;
/**
 * Get user by email query
 * <p>
 *     This class represents the query to get a user by its email.
 * </p>
 * @param email the username of the user
 */
public record GetUserByEmailQuery(String email) {
}

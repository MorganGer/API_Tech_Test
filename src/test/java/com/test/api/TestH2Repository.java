package com.test.api;

import com.test.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<User, Integer> {
}

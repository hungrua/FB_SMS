package com.signupfacebook.Newlife_project_1.repository.repository1;

import com.signupfacebook.Newlife_project_1.model.entity1.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    @Query(value = "select * from api_key order by(rand()) limit 1", nativeQuery = true)
    Optional<ApiKeyEntity> findOneRanDom();
}

package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false, length = 10)
    private String userId; //users table -> user_id : pk, not null, varchar(10)

    @Column(name = "password", nullable = false, length = 60)
    private String password; //users table -> password : not null, varchar(100)

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname; //users table -> nickname : not null, varchar(10)

    @Column(name = "role", nullable = false, length = 10)
    private String role; //users table -> role : not null, varchar(10)

    //UserEntity(테이블 users)와 PetEntity(테이블 pets)를 0ne to many 관계로 매핑
    //부모 엔티티가 삭제될 때와 부모 엔티티의 컬렉션에서 자식 엔티티가 제거될 때 모두 자식 엔티티가 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PetEntity> pets;

    //UserEntity(테이블 users)와 AlarmSettingEntity(테이블 alarm_settings)를 0ne to one 관계로 매핑
    //UserEntity가 삭제되거나 AlarmSettingEntity와의 관계가 제거되면 관련된 AlarmSettingEntity가 데이터베이스에서 자동으로 삭제
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private AlarmSettingEntity alarmSetting;

    //UserEntity(테이블 users)와 WalkingPostEntity(테이블 walking_posts)를 0ne to many 관계로 매핑
    //부모 엔티티가 삭제될 때와 부모 엔티티의 컬렉션에서 자식 엔티티가 제거될 때 모두 자식 엔티티가 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<WalkingPostEntity> walkingPosts;

    //UserEntity(테이블 users)와 WalkingPostEntity(테이블 walking_posts)를 0ne to many 관계로 매핑
    //부모 엔티티가 삭제될 때와 부모 엔티티의 컬렉션에서 자식 엔티티가 제거될 때 모두 자식 엔티티가 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<WalkingPostCommentEntity> walkingPostComments;
}

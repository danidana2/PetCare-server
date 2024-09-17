package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alarm_settings")
public class AlarmSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_setting_id", nullable = false)
    private int alarmSettingId; //alarm_settings table -> alarm_setting_id : pk, not null, auto increment, INT

    @Column(name = "insulin_time_alarm", nullable = false)
    private Boolean insulinTimeAlarm; //alarm_settings table -> insulin_time_alarm : not null, TINYINT(1)

    @Column(name = "next_visit_alarm", nullable = false)
    private Boolean nextVisitAlarm; //alarm_settings table -> next_visit_alarm : not null, TINYINT(1)

    @Column(name = "heartworm_alarm", nullable = false)
    private Boolean heartwormAlarm; //alarm_settings table -> heartworm_alarm : not null, TINYINT(1)

    @Column(name = "walking_alarm", nullable = false)
    private Boolean walkingAlarm; //alarm_settings table -> exercise_alarm : not null, TINYINT(1)

    //UserEntity(테이블 users)와 AlarmSettingEntity(테이블 alarm_settings)를 0ne to one 관계로 매핑
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
}

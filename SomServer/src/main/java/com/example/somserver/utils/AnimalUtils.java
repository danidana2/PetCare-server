package com.example.somserver.utils;

import java.util.Arrays;
import java.util.List;

public class AnimalUtils {

    // 강아지 품종 리스트
    private static final List<String> DOG_BREEDS = Arrays.asList(
            "치와와", "말티즈", "포메",
            "비숑", "푸들(소형)", "푸들(중형)", "푸들(대형)"
    );

    // 고양이 품종 리스트
    private static final List<String> CAT_BREEDS = Arrays.asList(
            "코숏", "스코티쉬폴드", "터키쉬앙고라",
            "페르시안", "러시안블루"
    );

    // 인스턴스화 방지
    private AnimalUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // 품종에 따라 강아지인지 고양이인지 구분하는 메서드
    public static String getAnimalType(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            return "Unknown";
        }

        if (DOG_BREEDS.contains(breed)) {
            return "Dog";
        } else if (CAT_BREEDS.contains(breed)) {
            return "Cat";
        } else {
            return "Unknown";
        }
    }
}

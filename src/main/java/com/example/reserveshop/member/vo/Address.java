package com.example.reserveshop.member.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String value;

    public Address(String value) {
        validate();
        this.value = value;
    }

    private void validate() {}

    public static Address of(String value) {
        return new Address(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address loginId = (Address) o;
        return Objects.equals(value, loginId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

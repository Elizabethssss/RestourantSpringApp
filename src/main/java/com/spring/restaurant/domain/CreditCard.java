package com.spring.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    @Pattern(regexp = "[0-9]{13,16}", message = "Wrong input!")
    @CreditCardNumber(message = "Wrong number!")
    private String creditCardNumber;

    @NotBlank(message = "Empty field!")
    private String ccMonth;

    @NotBlank(message = "Empty field!")
    private String ccYear;

    @Pattern(regexp = "[0-9]{3}", message = "Wrong input!")
    private String cvv;

    public boolean isDateValid() {
        final Integer year = LocalDate.now().getYear() - 2000;
        final Integer monthValue = LocalDate.now().getMonthValue();
        if(!ccYear.matches("([2-3][0-9])") || !ccMonth.matches("(0[1-9]|1[0-2])")) {
            return false;
        }
        return year.compareTo(Integer.valueOf(ccYear)) <= 0 && monthValue.compareTo(Integer.valueOf(ccMonth)) < 0;
    }
}

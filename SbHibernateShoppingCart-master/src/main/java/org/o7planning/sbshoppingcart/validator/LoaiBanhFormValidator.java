package org.o7planning.sbshoppingcart.validator;

import org.o7planning.sbshoppingcart.dao.LoaiBanhDAO;
import org.o7planning.sbshoppingcart.entity.LoaiBanh;
import org.o7planning.sbshoppingcart.form.LoaiBanhForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LoaiBanhFormValidator implements Validator {

    @Autowired
    private LoaiBanhDAO loaiBanhDAO;

    // This validator only checks for the ProductForm.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == LoaiBanhForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoaiBanhForm loaiBanhForm = (LoaiBanhForm) target;

        // Check the fields of ProductForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "LoaiBanhId", "NotEmpty.LoaiBanhForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "TenLoaiBah", "NotEmpty.LoaiBanhForm.name");

        String code = loaiBanhForm.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.loaiBanhForm.code");
            } else if (loaiBanhForm.isNewLoaiBanh()) {
                LoaiBanh loaibanh = loaiBanhDAO.findLoaiBanh(code);
                if (loaibanh != null) {
                    errors.rejectValue("code", "Duplicate.loaiBanhForm.code");
                }
            }
        }
    }

}
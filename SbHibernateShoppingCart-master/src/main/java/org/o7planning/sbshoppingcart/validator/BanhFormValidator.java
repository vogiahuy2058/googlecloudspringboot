package org.o7planning.sbshoppingcart.validator;

import org.o7planning.sbshoppingcart.dao.BanhDAO;
import org.o7planning.sbshoppingcart.entity.Banh;
import org.o7planning.sbshoppingcart.form.BanhForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

    @Component
    public class BanhFormValidator implements Validator {

        @Autowired
        private BanhDAO banhDAO;

        // This validator only checks for the ProductForm.
        @Override
        public boolean supports(Class<?> clazz) {
            return clazz == BanhForm.class;
        }

        @Override
        public void validate(Object target, Errors errors) {
            BanhForm banhForm = (BanhForm) target;

            // Check the fields of ProductForm.
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "BanhId", "NotEmpty.BanhForm.code");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "TenBanh", "NotEmpty.BanhForm.name");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "DonGia", "NotEmpty.BanhForm.price");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "LoaiBanhId", "NotEmpty.BanhForm.loaiBanh");

            String code = banhForm.getCode();
            if (code != null && code.length() > 0) {
                if (code.matches("\\s+")) {
                    errors.rejectValue("code", "Pattern.BanhForm.code");
                } else if (banhForm.isNewBanh()) {
                    Banh banh = banhDAO.findBanh(code);
                    if (banh != null) {
                        errors.rejectValue("code", "Duplicate.BanhForm.code");
                    }
                }
            }
        }

    }

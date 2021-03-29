package br.com.prevent.logmanager.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.resources.exception.FieldMessage;
import br.com.prevent.logmanager.service.validation.util.LogValidatorUtil;

public class LogValidator implements ConstraintValidator<LogValid, Log> {

	@Override
	public void initialize(LogValid constraintAnnotation) {
	}

	@Override
	public boolean isValid(Log log, ConstraintValidatorContext context) {

		List<FieldMessage> msgs = new ArrayList<>();
		LogValidatorUtil validator = new LogValidatorUtil();

		if (!validator.isIpValid(log.getIp())) {
			msgs.add(new FieldMessage("ip", LogValidatorUtil.MSG_IP_INVALIDO));
		}

		if (!validator.isStatusValid(log.getStatus())) {
			msgs.add(new FieldMessage("status", LogValidatorUtil.MSG_STATUS_INVALIDO));
		}

		for (FieldMessage item : msgs) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(item.getMessage()).addPropertyNode(item.getFieldName())
					.addConstraintViolation();
		}
		return msgs.isEmpty();
	}

}

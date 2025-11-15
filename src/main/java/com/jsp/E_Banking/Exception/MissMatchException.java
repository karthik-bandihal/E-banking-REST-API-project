package com.jsp.E_Banking.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MissMatchException extends RuntimeException {
	private String message = "Missmatch";
}

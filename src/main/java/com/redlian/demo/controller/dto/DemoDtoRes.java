package com.redlian.demo.controller.dto;

import java.util.List;

import com.redlian.demo.service.NewCoinRes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DemoDtoRes {

	private List<NewCoinRes> newCoinResList;
}

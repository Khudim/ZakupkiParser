package com.khudim.parser;

import com.khudim.filter.ParseResult;
import com.khudim.filter.Requirement;

/**
 * Created by Beaver.
 */
public interface IParser {

    ParseResult parse(Requirement requirement);
}

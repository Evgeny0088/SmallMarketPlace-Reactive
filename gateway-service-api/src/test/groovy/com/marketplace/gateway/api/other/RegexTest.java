package com.marketplace.gateway.api.other;

import org.junit.jupiter.api.Test;
import com.marketplace.gateway.api.util.RegexUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.marketplace.gateway.api.common.Constants.PATH_PATTERN;

public class RegexTest {

    @Test
    public void ruleRegexTest() {
        String regex = RegexUtil.ruleToRegex("/abracadabra/*/world");
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world/").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/world/1").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/kevin").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hel/lo/world").matches();

        regex = RegexUtil.ruleToRegex("/abracadabra/*/world/");
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world/").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/world/1").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/kevin").matches();

        regex = RegexUtil.ruleToRegex("/abracadabra/**/world");
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world/").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/world/1").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/kevin").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hel/lo/world").matches();

        regex = RegexUtil.ruleToRegex("/abracadabra/hello/*");
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world/").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/bye-bye/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/wor/ld").matches();

        regex = RegexUtil.ruleToRegex("/abracadabra/hello/*/");
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world/").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/bye-bye/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/hello/wor/ld").matches();

        regex = RegexUtil.ruleToRegex("/abracadabra/hello/**");
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world/").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hello/world").matches();
        assert !Pattern.compile(regex).matcher("/abracadabra/bye-bye/world").matches();
        assert Pattern.compile(regex).matcher("/abracadabra/hello/wor/ld").matches();
    }

    @Test
    public void pathRegexTest() {
        Matcher matcher = PATH_PATTERN.matcher("/tim-auth-registration-api/api/v1/registration/6239d4acc69b1842b9504864/register");
        assert matcher.find();
        assert matcher.group("service").equals("tim-auth-registration-api");
        assert matcher.group("path").equals("/api/v1/registration/6239d4acc69b1842b9504864/register");
    }
}

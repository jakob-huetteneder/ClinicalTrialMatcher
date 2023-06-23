package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FaqDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.FaqService;
import at.ac.tuwien.sepm.groupphase.backend.util.GetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class FaqServiceImpl implements FaqService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final static GetProperties getProperties = new GetProperties();
    private final static Properties prop = getProperties.readProperties();
    private final AuthorizationService authorizationService;

    @Autowired
    public FaqServiceImpl(UserRepository userRepository, AuthorizationService authorizationService,
                          UserMapper userMapper, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public FaqDto getFaqAnswer(String message, String role) {
        LOG.trace("getFaqAnswer({})", message);
        if ("xintro".equals(message) || "start".equals(message)) {
            String answer = "xintro".equals(message) ? prop.getProperty("welcome.message") : prop.getProperty("start.message");
            Map<String, String> buttons = new HashMap<>();
            for (Object o : prop.keySet()) {
                String item = o.toString();
                if (item.contains("start." + role + ".button") && item.contains("message")) {
                    buttons.put(prop.getProperty(item.replace("message", "key")), prop.getProperty(item));
                }
            }
            return new FaqDto(answer, buttons);
        } else if (prop.containsKey(message + ".message")) {
            String answer = prop.getProperty(message + ".message");
            if (prop.containsKey(message + ".button.0.message")) {
                Map<String, String> buttons = new HashMap<>();
                for (Object o : prop.keySet()) {
                    String item = o.toString();
                    if (item.substring(0, item.indexOf(".")).equals(message) && item.contains("button") && item.contains("message")) {
                        buttons.put(prop.getProperty(item.replace("message", "key")), prop.getProperty(item));
                    }
                }
                return new FaqDto(answer, buttons);
            }
            return new FaqDto(answer, null);
        } else {
            return new FaqDto(prop.getProperty("notfound.message"), null);
        }
    }
}

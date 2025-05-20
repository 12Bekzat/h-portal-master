package com.example.demo.Migrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.Models.DocumentTemplate;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.IDocumentTemplateRepository;
import com.example.demo.Repository.IRoleRepository;
import com.example.demo.Repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseSeed implements CommandLineRunner {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IDocumentTemplateRepository documentTemplateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        CreateRoles();
        CreateUsers();
        CreateDocumentTemplates();
        System.out.println("Database seed successfully");
    }

    private void CreateRoles() {
        List<Role> all = roleRepository.findAll();

        if (!all.isEmpty()) return;

        List<Role> roles = List.of(
                new Role("ROLE_ADMIN"),
                new Role("ROLE_TEACHER"),
                new Role("ROLE_STUDENT")
        );

        roleRepository.saveAll(roles);
    }

    private void CreateUsers() {
        List<User> all = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        boolean hasAdmin = all.stream()
                .flatMap(user -> user.getRoles().stream())
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (hasAdmin) return;

        Role admin = roles.stream()
                .filter(role -> "ROLE_ADMIN".equals(role.getName()))
                .findFirst()
                .orElse(null);

        if (admin == null) return;

        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin123"));
        user.setEmail("admin@gmail.com");
        user.setRoles(List.of(admin));

        userRepository.save(user);
    }

    private void CreateDocumentTemplates() {
        DocumentTemplate place_of_study = new DocumentTemplate();
        place_of_study.setName("place_of_study");
        place_of_study.setDescription("Этот документ подтверждает, что вы официально обучаетесь в МУИТ.");
        place_of_study.setRequiresApproval(false);
        place_of_study.setContentTemplate("<!DOCTYPE html>\n" +
                "<html lang=\"ru\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\"/>\n" +
                "    <style>\n" +
                "       @font-face {\n" +
                        "    font-family: 'timesnewroman';\n" +
                        "    src: url('fonts/timesnewroman.ttf');\n" +
                        "}\n" +
                "        body {\n" +
                "            font-family: \"InterRegular\";\n" +
                "            font-size: 14pt;\n" +
                "            line-height: 1.6;\n" +
                "            padding: 40px;\n" +
                "            position: relative;\n" +
                "            color: black;\n" +
                "        }\n" +
                "        img {\n" +
                "        margin-top: 30px\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            font-weight: bold;\n" +
                "            font-size: 16pt;\n" +
                "            margin-bottom: 30px;\n" +
                "            color: black;\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: justify;\n" +
                "            color: black;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 60px;\n" +
                "            font-size: 12pt;\n" +
                "            color: black;\n" +
                "        }\n" +
                "        .seal {\n" +
                "            position: absolute;\n" +
                "            bottom: 40px;\n" +
                "            right: 60px;\n" +
                "            opacity: 0.7;\n" +
                "            width: 120px;\n" +
                "            color: black;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <div class=\"header\">\n" +
                "        СПРАВКА<br/>\n" +
                "        с места учебы\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"content\">\n" +
                "        Настоящая справка выдана <strong>{{fullName}}</strong>,<br/>\n" +
                "        студенту <strong>{{courseName}}</strong> <strong>{{courseYear}}</strong> курса,<br/>\n" +
                "        обучающемуся в нашем учебном заведении на основании заявления от <strong>{{requestDate}}</strong>.\n" +
                "\n" +
                "        <br/><br/>\n" +
                "        Справка выдана для предоставления по месту требования.\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"footer\">\n" +
                "        Директор университета<br/>\n" +
                "    </div>\n" +
                "\n" +
                "    <img src=\"https://www.mplux.kz/images/obrazci/too/obrazec_too-1.jpg\" alt=\"Печать\" class=\"seal\" />\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        DocumentTemplate place_of_work = new DocumentTemplate();
        place_of_work.setName("place_of_work");
        place_of_work.setDescription("Этот документ подтверждает, что вы действительно работаете\n");
        place_of_work.setRequiresApproval(false);
        place_of_work.setContentTemplate("<!DOCTYPE html>\n" +
                "<html lang=\"ru\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\"/>\n" +
                "    <style>\n" +
                "       @font-face {\n" +
                "    font-family: 'timesnewroman';\n" +
                "    src: url('fonts/timesnewroman.ttf');\n" +
                "}\n" +
                "        body {\n" +
                "            font-family: \"timesnewroman\";\n" +
                "            font-size: 14pt;\n" +
                "            line-height: 1.6;\n" +
                "            padding: 40px;\n" +
                "            position: relative;\n" +
                "        }\n" +
                "        img {\n" +
                "        margin-top: 30px\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            font-weight: bold;\n" +
                "            font-size: 16pt;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: justify;\n" +
                "            color: black;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 60px;\n" +
                "            font-size: 12pt;\n" +
                "        }\n" +
                "        .seal {\n" +
                "            position: absolute;\n" +
                "            bottom: 40px;\n" +
                "            right: 60px;\n" +
                "            opacity: 0.7;\n" +
                "            width: 120px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <div class=\"header\">\n" +
                "        СПРАВКА<br/>\n" +
                "        с места работы\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"content\">\n" +
                "        Настоящая справка выдана <strong>{{fullName}}</strong>,<br/>\n" +
                "        занимающему должность <strong>{{position}}</strong><br/>\n" +
                "        в <strong>{{department}}</strong> учебного заведения.<br/><br/>\n" +
                "\n" +
                "        {{fullName}} работает с <strong>{{employmentStartDate}}</strong> по настоящее время.\n" +
                "\n" +
                "        <br/><br/>\n" +
                "        Справка выдана по месту требования на основании обращения от <strong>{{requestDate}}</strong>.\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"footer\">\n" +
                "        Отдел кадров<br/>\n" +
                "        _____________________________\n" +
                "    </div>\n" +
                "\n" +
                "    <img src=\"https://www.mplux.kz/images/obrazci/too/obrazec_too-1.jpg\" alt=\"Печать\" class=\"seal\" />\n" +
                "\n" +
                "</body>\n" +
                "</html>\n");
        DocumentTemplate application_vacation = new DocumentTemplate();
        application_vacation.setName("application_vacation");
        application_vacation.setDescription("Это заявление выражает ваше желание получить ежегодный трудовой отпуск. Заявка будет рассмотрена администрацией, после чего может быть одобрена или отклонена.\n");
        application_vacation.setRequiresApproval(true);
        application_vacation.setContentTemplate("<!DOCTYPE html>\n" +
                "<html lang=\"ru\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\"/>\n" +
                "    <style>\n" +
                "       @font-face {\n" +
                "    font-family: 'timesnewroman';\n" +
                "    src: url('fonts/timesnewroman.ttf');\n" +
                "}\n" +
                "        body {\n" +
                "        img {\n" +
                "        margin-top: 30px\n" +
                "        }\n" +
                "            font-family: \"timesnewroman\";\n" +
                "            font-size: 14pt;\n" +
                "            line-height: 1.6;\n" +
                "            padding: 40px;\n" +
                "            position: relative;\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            font-weight: bold;\n" +
                "            font-size: 16pt;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: justify;\n" +
                "            color: black;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 60px;\n" +
                "            font-size: 12pt;\n" +
                "        }\n" +
                "        .approved-stamp {\n" +
                "            margin-top: 40px;\n" +
                "            font-weight: bold;\n" +
                "            color: green;\n" +
                "        }\n" +
                "        .seal {\n" +
                "            position: absolute;\n" +
                "            bottom: 40px;\n" +
                "            right: 60px;\n" +
                "            opacity: 0.7;\n" +
                "            width: 120px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <div class=\"header\">\n" +
                "        ЗАЯВЛЕНИЕ\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"content\">\n" +
                "        Прошу предоставить мне отпуск с {{startDate}} по {{endDate}}<br/>\n" +
                "        в связи с {{reason}}.\n" +
                "\n" +
                "        <br/><br/>\n" +
                "        ФИО сотрудника: {{fullName}}<br/>\n" +
                "        Должность: {{position}}<br/>\n" +
                "        Дата подачи заявления: {{requestDate}}\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"approved-stamp\">\n" +
                "        Заявление {{approvedText}}<br/>\n" +
                "        {{approvalDate}}\n" +
                "    </div>\n" +
                "    <img src=\"https://www.mplux.kz/images/obrazci/too/obrazec_too-1.jpg\" alt=\"Печать\" class=\"seal\" />\n" +
                "\n" +
                "</body>\n" +
                "</html>\n");


        List<DocumentTemplate> all = new ArrayList<>();

        Optional<DocumentTemplate> placeOfWork = documentTemplateRepository.findByName("place_of_work");
        Optional<DocumentTemplate> placeOfStudy = documentTemplateRepository.findByName("place_of_study");
        Optional<DocumentTemplate> applicationVacation = documentTemplateRepository.findByName("application_vacation");
        if(placeOfWork.isEmpty()) {
            all.add(place_of_work);
        }
        if(placeOfStudy.isEmpty()) {
            all.add(place_of_study);
        }
        if(applicationVacation.isEmpty()) {
            all.add(application_vacation);
        }

        documentTemplateRepository.saveAll(all);
    }
}

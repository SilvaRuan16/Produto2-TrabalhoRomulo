package com.boletin.aplication.ocorrencia;

import com.boletin.aplication.ocorrencia.models.BoletinModel;
import com.boletin.aplication.ocorrencia.models.UserModel;
import com.boletin.aplication.ocorrencia.services.BoletinService;
import com.boletin.aplication.ocorrencia.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class MenuRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private BoletinService boletinService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserModel loggedUser = null;
    private final Scanner scan = new Scanner(System.in);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void run(String... args) {
        int option;
        do {
            showMenu();
            option = readOption();
            executeOption(option);
        } while (option != 0);
        System.out.println("Sistema encerrado. Até logo!");
        scan.close();
    }

    private void showMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    SISTEMA DE BOLETIM DE OCORRÊNCIA");
        System.out.println("=".repeat(60));
        System.out.println("1. Registrar Usuário");
        System.out.println("2. Fazer Login");
        if (loggedUser != null) {
            System.out.println("3. Registrar Boletim");
            System.out.println("4. Meus Boletins");
            System.out.println("5. Listar Todos Boletins");
            System.out.println("6. Atualizar Meus Dados");
            System.out.println("7. Excluir Minha Conta");
            System.out.println("8. Atualizar Boletim");
            System.out.println("9. Excluir Boletim");
        }
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int readOption() {
        while (!scan.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scan.next();
        }
        int op = scan.nextInt();
        scan.nextLine();
        return op;
    }

    private void executeOption(int option) {
        switch (option) {
            case 1 -> insertUser();
            case 2 -> loginUser();
            case 3 -> insertBoletin();
            case 4 -> readUserBoletin();
            case 5 -> readAllBoletins();
            case 6 -> updateUser();
            case 7 -> deleteUser();
            case 8 -> updateBoletin();
            case 9 -> deleteBoletin();
            case 0 -> {}
            default -> System.out.println("Opção inválida!");
        }
    }

    // ====================== USUÁRIO ======================

    private void insertUser() {
        System.out.print("Nome de usuário: ");
        String userName = scan.nextLine();
        if (userService.Read().stream().anyMatch(u -> u.getUserName().equals(userName))) {
            System.out.println("Este usuário já existe!");
            return;
        }
        System.out.print("Senha: ");
        String password = scan.nextLine();

        UserModel user = new UserModel();
        user.setUserName(userName);
        user.setPassword(password);
        userService.Inserir(user);
        System.out.println("Usuário registrado com sucesso!");
    }

    private void loginUser() {
        System.out.print("Usuário: ");
        String userName = scan.nextLine();
        System.out.print("Senha: ");
        String password = scan.nextLine();

        loggedUser = userService.Read().stream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst()
                .orElse(null);

        if (loggedUser != null && passwordEncoder.matches(password, loggedUser.getPassword())) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + userName + "!");
        } else {
            System.out.println("Usuário ou senha incorretos.");
            loggedUser = null;
        }
    }

    private void updateUser() {
        if (loggedUser == null) {
            System.out.println("Faça login primeiro!");
            return;
        }
        System.out.println("Atualizar dados do usuário: " + loggedUser.getUserName());
        System.out.print("Novo nome de usuário (ou Enter para manter): ");
        String newName = scan.nextLine();
        System.out.print("Nova senha (ou Enter para manter): ");
        String newPass = scan.nextLine();

        UserModel updated = new UserModel();
        updated.setUserName(newName.isBlank() ? loggedUser.getUserName() : newName);
        updated.setPassword(newPass.isBlank() ? null : newPass);

        userService.Update(loggedUser.getId(), updated);
        if (!newName.isBlank()) loggedUser.setUserName(updated.getUserName());
        System.out.println("Dados atualizados com sucesso!");
    }

    private void deleteUser() {
        if (loggedUser == null) {
            System.out.println("Faça login primeiro!");
            return;
        }
        System.out.print("Tem certeza que deseja EXCLUIR sua conta? (s/N): ");
        if (!scan.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Exclusão cancelada.");
            return;
        }
        getBoletinOfUser().forEach(b -> boletinService.Deletar(b.getId()));
        userService.Deletar(loggedUser.getId());
        System.out.println("Conta e boletins excluídos com sucesso.");
        loggedUser = null;
    }

    // ====================== BOLETIM ======================

    private void insertBoletin() {
        if (loggedUser == null) {
            System.out.println("Faça login primeiro!");
            return;
        }
        System.out.print("Título: ");
        String title = scan.nextLine();
        System.out.print("Descrição: ");
        String description = scan.nextLine();

        BoletinModel boletin = new BoletinModel();
        boletin.setTitle(title);
        boletin.setDescription(description);
        boletin.setDateRegistry(LocalDateTime.now());
        boletin.setUsuarioModel(loggedUser);

        boletinService.Inserir(boletin);
        System.out.println("Boletim registrado com sucesso!");
    }

    private void readUserBoletin() {
        if (loggedUser == null) {
            System.out.println("Faça login primeiro!");
            return;
        }
        List<BoletinModel> meus = getBoletinOfUser();
        if (meus.isEmpty()) {
            System.out.println("Você não tem boletins registrados.");
            return;
        }
        System.out.println("\nSeus Boletins:");
        meus.forEach(b -> System.out.printf("• %s | %s | %s%n",
                b.getTitle(), b.getDescription(), b.getDateRegistry().format(dtf)));
    }

    private void readAllBoletins() {
        List<BoletinModel> todos = boletinService.Read();
        if (todos.isEmpty()) {
            System.out.println("Nenhum boletim registrado.");
            return;
        }
        System.out.println("\nTodos os Boletins:");
        todos.forEach(b -> System.out.printf("• [%s] %s | %s | %s%n",
                b.getUsuarioModel().getUserName(),
                b.getTitle(), b.getDescription(), b.getDateRegistry().format(dtf)));
    }

    private void updateBoletin() {
        if (loggedUser == null) {
            System.out.println("Faça login primeiro!");
            return;
        }
        List<BoletinModel> meus = getBoletinOfUser();
        if (meus.isEmpty()) {
            System.out.println("Você não tem boletins para atualizar.");
            return;
        }
        System.out.println("Seus boletins:");
        for (int i = 0; i < meus.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, meus.get(i).getTitle());
        }
        System.out.print("Número do boletim: ");
        int index = readOption() - 1;
        if (index < 0 || index >= meus.size()) {
            System.out.println("Número inválido.");
            return;
        }
        BoletinModel b = meus.get(index);
        System.out.print("Novo título (Enter para manter): ");
        String t = scan.nextLine();
        System.out.print("Nova descrição (Enter para manter): ");
        String d = scan.nextLine();

        BoletinModel atualizado = new BoletinModel();
        atualizado.setTitle(t.isBlank() ? b.getTitle() : t);
        atualizado.setDescription(d.isBlank() ? b.getDescription() : d);

        boletinService.Update(b.getId(), atualizado);
        System.out.println("Boletim atualizado!");
    }

    private void deleteBoletin() {
        if (loggedUser == null) {
            System.out.println("Faça login primeiro!");
            return;
        }
        List<BoletinModel> meus = getBoletinOfUser();
        if (meus.isEmpty()) {
            System.out.println("Você não tem boletins para excluir.");
            return;
        }
        System.out.println("Seus boletins:");
        for (int i = 0; i < meus.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, meus.get(i).getTitle());
        }
        System.out.print("Número do boletim para EXCLUIR: ");
        int index = readOption() - 1;
        if (index < 0 || index >= meus.size()) {
            System.out.println("Número inválido.");
            return;
        }
        System.out.print("Confirma? (s/N): ");
        if (scan.nextLine().equalsIgnoreCase("s")) {
            boletinService.Deletar(meus.get(index).getId());
            System.out.println("Boletim excluído!");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private List<BoletinModel> getBoletinOfUser() {
        return boletinService.Read().stream()
                .filter(b -> b.getUsuarioModel() != null && b.getUsuarioModel().getId().equals(loggedUser.getId()))
                .toList();
    }
}
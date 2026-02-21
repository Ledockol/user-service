package com.example;

import com.example.dao.UserDaoImpl;
import com.example.models.User;
import com.example.services.UserService;
import com.example.services.UserServiceImpl;
import com.example.util.HibernateSessionFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final UserService userService = new UserServiceImpl(new UserDaoImpl(HibernateSessionFactoryUtil.getSessionFactory()));

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;

            while (!exit) {
                System.out.println("\nМеню:");
                System.out.println("1. Создать нового пользователя");
                System.out.println("2. Получить пользователя по ID");
                System.out.println("3. Получить всех пользователей");
                System.out.println("4. Обновить пользователя");
                System.out.println("5. Удалить пользователя");
                System.out.println("6. Найти по email");
                System.out.println("7. Найти по имени");
                System.out.println("8. Найти по возрасту");
                System.out.println("0. Выход");

                System.out.print("Выберите действие: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        createUser(scanner);
                        break;
                    case "2":
                        getUserById(scanner);
                        break;
                    case "3":
                        showAllUsers();
                        break;
                    case "4":
                        updateUser(scanner);
                        break;
                    case "5":
                        deleteUser(scanner);
                        break;
                    case "6":
                        findByEmail(scanner);
                        break;
                    case "7":
                        findByName(scanner);
                        break;
                    case "8":
                        findByAge(scanner);
                        break;
                    case "0":
                        exit = true;
                        break;
                    default:
                        System.out.println("Неверный выбор. Попробуйте еще раз.");
                }
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка: ", e);
        } finally {
            HibernateSessionFactoryUtil.shutdown();
        }
    }

    public static void createUser(Scanner scanner) {
        try {
            System.out.print("Введите имя: ");
            String name = scanner.nextLine();

            System.out.print("Введите email: ");
            String email = scanner.nextLine();

            System.out.print("Введите возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            User user = new User(name, email, age);
            User createdUser = userService.createUser(user);

            if (createdUser != null) {
                System.out.println("Пользователь создан: " + createdUser);
            } else {
                System.out.println("Ошибка при создании пользователя");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: возраст должен быть числом");
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    public static void showAllUsers() {
        List<User> users = userService.getAllUsers();

        if (!users.isEmpty()) {
            System.out.println("Список всех пользователей:");
            for (User user : users) {
                System.out.println(user);
            }
        } else {
            System.out.println("Пользователей не найдено");
        }
    }

    public static void getUserById(Scanner scanner) {
        try {
            System.out.print("Введите ID пользователя: ");
            Long id = Long.parseLong(scanner.nextLine());
            User user = userService.getUserById(id);

            if (user != null) {
                System.out.println("Найден пользователь: " + user);
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при получении пользователя: " + e.getMessage());
        }
    }

    public static void updateUser(Scanner scanner) {
        try {
            System.out.print("Введите ID пользователя для обновления: ");
            Long id = Long.parseLong(scanner.nextLine());
            User user = userService.getUserById(id);

            if (user != null) {
                System.out.println("Текущий пользователь: " + user);

                System.out.print("Введите новое имя (оставить пустым для сохранения): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) {
                    user.setName(name);
                }

                System.out.print("Введите новый email (оставить пустым для сохранения): ");
                String email = scanner.nextLine();
                if (!email.isEmpty()) {
                    user.setEmail(email);
                }

                System.out.print("Введите новый возраст (оставить пустым для сохранения): ");
                String ageInput = scanner.nextLine();
                if (!ageInput.isEmpty()) {
                    user.setAge(Integer.parseInt(ageInput));
                }

                User updatedUser = userService.updateUser(user);
                if (updatedUser != null) {
                    System.out.println("Пользователь успешно обновлен: " + updatedUser);
                } else {
                    System.out.println("Ошибка при обновлении пользователя");
                }
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID и возраст должны быть числами");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при обновлении: " + e.getMessage());
        }
    }

    public static void deleteUser(Scanner scanner) {
        try {
            System.out.print("Введите ID пользователя для удаления: ");
            Long id = Long.parseLong(scanner.nextLine());
            User user = userService.getUserById(id);

            if (user != null) {
                userService.deleteUser(id);
                System.out.println("Пользователь с ID " + id + " успешно удален");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при удалении: " + e.getMessage());
        }
    }

    public static void findByEmail(Scanner scanner) {
        try {
            System.out.print("Введите email для поиска: ");
            String email = scanner.nextLine();
            User user = userService.findByEmail(email);

            if (user != null) {
                System.out.println("Найден пользователь: " + user);
            } else {
                System.out.println("Пользователь с email " + email + " не найден");
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка при поиске по email: " + e.getMessage());
        }
    }

    public static void findByName(Scanner scanner) {
        try {
            System.out.print("Введите имя для поиска: ");
            String name = scanner.nextLine();
            List<User> users = userService.findUsersByName(name);

            if (!users.isEmpty()) {
                System.out.println("Найдено пользователей: " + users.size());
                for (User user : users) {
                    System.out.println(user);
                }
            } else {
                System.out.println("Пользователи с именем '" + name + "' не найдены");
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка при поиске по имени: " + e.getMessage());
        }
    }

    public static void findByAge(Scanner scanner) {
        try {
            System.out.print("Введите возраст для поиска: ");
            int age = Integer.parseInt(scanner.nextLine());
            List<User> users = userService.findUsersByAge(age);

            if (!users.isEmpty()) {
                System.out.println("Найдено пользователей возрастом " + age + ": " + users.size());
                for (User user : users) {
                    System.out.println(user);
                }
            } else {
                System.out.println("Пользователи с возрастом " + age + " не найдены");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: возраст должен быть числом");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при поиске по возрасту: " + e.getMessage());
        }
    }
}
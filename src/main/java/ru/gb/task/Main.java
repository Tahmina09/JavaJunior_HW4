package ru.gb.task;


import ru.gb.models.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/";
        String root = "root";
        String password = "password";

        try{
            // Подключение к базе данных
            Connection connection = DriverManager.getConnection(url, root, password);

            //Создание баз данных
            createDatabase(connection);
            System.out.println("Database created successfully!");

            //Использование базы данных
            useDatabase(connection);
            System.out.println("Use database successfully!");

            //Создание таблицы
            createTable(connection);
            System.out.println("Table created successfully!");

            //Добавление данных
            int count = 6;
            for (int i = 1; i < count; i++) {
                insertData(connection, Course.createCourses());
            }
            System.out.println("Data is successfully inserted!");

            //Чтение данных
            Collection<Course> courses = readData(connection);
            for (var course: courses) {
                System.out.println(course);
            }
            System.out.println("Read data successfully!");

            //Обновление данных
            for (var course: courses) {
                updateData(connection, course);
            }
            System.out.println("Data updated successfully!");

            for (var course: courses) {
                System.out.println(course);
            }
            System.out.println("Read data successfully!");

            //Удаление данных
            for (var course: courses) {
                deleteData(connection, course.getId());
            }
            System.out.println("Data successfully deleted!");

            //Закрытие соединение
            connection.close();
            System.out.println("Connection successfully closed!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //region Вспомогательные методы

    private static void createDatabase(Connection connection) throws SQLException {
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS SchoolDB;";
        try(PreparedStatement statement = connection.prepareStatement(createDatabaseQuery)) {
            statement.execute();
        }
    }

    private static void useDatabase(Connection connection) throws SQLException {
        String useDatabaseQuery = "USE SchoolDB;";
        try(PreparedStatement statement = connection.prepareStatement(useDatabaseQuery)) {
            statement.execute();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Courses(id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), duration INT);";
        try(PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.execute();
        }
    }

    private static void insertData(Connection connection, Course course) throws SQLException {
        String insertDataQuery = "INSERT INTO Courses (title, duration) VALUES (?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(insertDataQuery)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.executeUpdate();
        }
    }

    private static Collection<Course> readData(Connection connection) throws SQLException {
        ArrayList<Course> courseList = new ArrayList<>();
        String readDataQuery = "SELECT * FROM Courses;";
        try(PreparedStatement statement = connection.prepareStatement(readDataQuery)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int duration = resultSet.getInt("duration");
                courseList.add(new Course(id, title, duration));
            }
            return courseList;
        }
    }

    private static void updateData(Connection connection, Course course) throws SQLException {
        String updateDataQuery = "UPDATE Courses SET title=?, duration=? WHERE id=?;";
        try(PreparedStatement statement = connection.prepareStatement(updateDataQuery)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        }
    }

    private static void deleteData(Connection connection, int id) throws SQLException {
        String deleteDataQuery = "DELETE FROM Courses WHERE id=?;";
        try(PreparedStatement statement = connection.prepareStatement(deleteDataQuery)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
    //endregion
}

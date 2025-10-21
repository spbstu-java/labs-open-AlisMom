package gui;

import hero.*;
import lab2.Invoker;
import streamapi.StreamTasks;
import translator.Dictionary;
import translator.DictionaryLoader;
import translator.FileReadException;
import translator.InvalidFileFormatException;
import translator.Translator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class App{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShow);
    }

    static void createAndShow() {
        JFrame f = new JFrame("Курсовая: задания 1–4");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(900, 650);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("1. Hero (Strategy)", heroTab());
        tabs.add("2. Аннотации", annotationTab());
        tabs.add("3. Переводчик", translatorTab());
        tabs.add("4. Stream API", streamTab());

        f.setContentPane(tabs);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    // ---------- TAB 1: Strategy ----------
    static JPanel heroTab() {
        JPanel p = panel();
        JTextArea out = readonlyArea();
        Position from = new Position(0, 0);
        Position to   = new Position(10, 5);
        Hero hero = new Hero(new Walk());

        ButtonGroup g = new ButtonGroup();
        JRadioButton rbWalk = new JRadioButton("Пешком", true);
        JRadioButton rbHorse = new JRadioButton("Лошадь");
        JRadioButton rbFly = new JRadioButton("Полёт");
        g.add(rbWalk); g.add(rbHorse); g.add(rbFly);

        JButton moveBtn = new JButton("Переместиться (0,0) → (10,5)");
        moveBtn.addActionListener(e -> {
            if (rbWalk.isSelected())  hero.setStrategy(new Walk());
            if (rbHorse.isSelected()) hero.setStrategy(new Horse());
            if (rbFly.isSelected())   hero.setStrategy(new Fly());
            String msg = hero.move(from, to);
            out.append(msg + "\n");
        });

        p.add(row(rbWalk, rbHorse, rbFly, moveBtn));
        p.add(scroll(out));
        return p;
    }

    // ---------- TAB 2: Аннотации ----------
    static JPanel annotationTab() {
        JPanel p = panel();
        JTextArea out = readonlyArea();
        JButton run = new JButton("Вызвать все аннотированные protected/private методы");
        run.addActionListener(e -> {
            List<String> logs = Invoker.runAll();
            logs.forEach(s -> out.append(s + "\n"));
        });
        p.add(row(run));
        p.add(scroll(out));
        return p;
    }

    // ---------- TAB 3: Переводчик ----------
    static JPanel translatorTab() {
        JPanel p = panel();
        JTextArea dictPath = readonlyArea(); dictPath.setText("Файл словаря: (не выбран)");
        JTextArea input = new JTextArea(8, 80);
        JTextArea output = readonlyArea();

        JButton pickDict = new JButton("Выбрать словарь...");
        final Dictionary[] holder = new Dictionary[1];

        pickDict.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
                Path path = fc.getSelectedFile().toPath();
                try {
                    holder[0] = DictionaryLoader.load(path);
                    dictPath.setText("Словарь: " + path);
                } catch (InvalidFileFormatException | FileReadException ex) {
                    output.append("Ошибка: " + ex.getMessage() + "\n");
                }
            }
        });

        JButton pickText = new JButton("Загрузить текст из файла...");
        pickText.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
                try {
                    String txt = java.nio.file.Files.readString(fc.getSelectedFile().toPath());
                    input.setText(txt);
                } catch (IOException ex) {
                    output.append("Ошибка чтения текста: " + ex.getMessage() + "\n");
                }
            }
        });

        JButton translate = new JButton("Перевести →");
        translate.addActionListener(e -> {
            output.setText("");
            if (holder[0] == null) {
                output.append("Сначала выберите словарь.\n");
                return;
            }
            Translator tr = new Translator(holder[0]);
            String[] lines = input.getText().split("\\R");
            for (String line : lines) {
                output.append(tr.translateLine(line) + "\n");
            }
        });

        p.add(row(pickDict, pickText, translate));
        p.add(scroll(dictPath));
        p.add(labeled("Ввод (ручной/из файла):", new JScrollPane(input)));
        p.add(labeled("Результат (read-only):", scroll(output)));
        return p;
    }

    // ---------- TAB 4: Stream API ----------
    static JPanel streamTab() {
        JPanel p = panel();
        JTextField intsField = new JTextField("1,2,2,3,4", 40);
        JTextField strsField = new JTextField("apple,banana,cherry", 40);
        JTextArea out = readonlyArea();

        JButton run = new JButton("Выполнить методы");
        run.addActionListener(e -> {
            try {
                List<Integer> nums = Arrays.stream(intsField.getText().split("\\s*,\\s*"))
                        .filter(s -> !s.isBlank())
                        .map(Integer::parseInt).collect(Collectors.toList());
                List<String> strs = Arrays.stream(strsField.getText().split("\\s*,\\s*"))
                        .filter(s -> !s.isBlank()).toList();

                out.setText("");
                out.append("Среднее: " + StreamTasks.average(nums) + "\n");
                out.append("В верхний регистр: " + StreamTasks.transformStrings(strs) + "\n");
                out.append("Уникальные квадраты: " + StreamTasks.uniqueSquares(nums) + "\n");
                out.append("Последний элемент: " + StreamTasks.getLast(strs) + "\n");
                int[] arr = nums.stream().mapToInt(Integer::intValue).toArray();
                out.append("Сумма чётных: " + StreamTasks.sumEven(arr) + "\n");
                out.append("Map из строк: " + StreamTasks.toMap(strs) + "\n");
            } catch (Exception ex) {
                out.append("Ошибка: " + ex.getMessage() + "\n");
            }
        });

        p.add(labeled("Список целых (через запятую):", intsField));
        p.add(labeled("Список строк (через запятую):", strsField));
        p.add(row(run));
        p.add(scroll(out));
        return p;
    }

    // ------- UI утилиты -------
    static JPanel panel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(12,12,12,12));
        return p;
    }
    static JScrollPane scroll(JTextArea ta) {
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(820, 240));
        return sp;
    }
    static JTextArea readonlyArea() {
        JTextArea ta = new JTextArea(8, 80);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }
    static JPanel row(Component... cs) {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Component c : cs) r.add(c);
        return r;
    }
    static JPanel labeled(String title, Component c) {
        JPanel r = new JPanel();
        r.setLayout(new BorderLayout(8,8));
        r.add(new JLabel(title), BorderLayout.NORTH);
        r.add(c, BorderLayout.CENTER);
        return r;
    }
}

package waterfall;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    private static Concealer concealer = new JPGConcealer();

    public static void main(String[] args) {
        generateSwingUI();
    }

    private static void generateSwingUI() {
        AtomicReference<String> filepath = new AtomicReference<>("");

        JFrame frame = new JFrame();
        frame.setSize(1000,500);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JTextField textField = new JTextField(10);
        textField.disable();
        textField.setBounds(290, 40, 560, 50);
        panel.add(textField);

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(290, 200, 560, 200);
        panel.add(textArea);

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select an image");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG images", "jpg");
        jfc.addChoosableFileFilter(filter);

        JButton imageChooserBtn = new JButton("Open file");
        imageChooserBtn.setBounds(100,40,180, 50);
        imageChooserBtn.addActionListener(e -> {
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                filepath.set(jfc.getSelectedFile().getPath());
                textField.setText(filepath.get());
            }
        });
        panel.add(imageChooserBtn);

        JButton concealBtn = new JButton("Conceal");
        concealBtn.setBounds(290, 100, 180, 50);
        concealBtn.addActionListener(e -> {
            concealer.conceal(filepath.get(), textArea.getText());
        });
        panel.add(concealBtn);

        JButton revealBtn = new JButton("Reveal");
        revealBtn.setBounds(478, 100, 180, 50);
        revealBtn.addActionListener(e -> {
            textArea.setText(concealer.reveal(filepath.get()));
        });
        panel.add(revealBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(668, 100, 180, 50);
        clearBtn.addActionListener(e -> {
            concealer.clean(filepath.get());
        });
        panel.add(clearBtn);

        Font jNewFont = imageChooserBtn.getFont().deriveFont(Font.PLAIN, imageChooserBtn.getFont().getSize() * getResolutionFactor());

        imageChooserBtn.setFont(jNewFont);
        textField.setFont(jNewFont);
        jfc.setFont(jNewFont);
        concealBtn.setFont(jNewFont);
        revealBtn.setFont(jNewFont);
        clearBtn.setFont(jNewFont);
        textArea.setFont(jNewFont);
    }

    private static float getResolutionFactor(){
        return ((float) Toolkit.getDefaultToolkit().getScreenResolution() / 80f);
    }
}

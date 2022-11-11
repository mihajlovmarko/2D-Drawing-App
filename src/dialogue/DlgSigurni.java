package dialogue;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgSigurni extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public boolean potvrda;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgSigurni dialog = new DlgSigurni();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgSigurni() {
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblBrisanjeOblika = new JLabel("Delete Shape");
			lblBrisanjeOblika.setHorizontalAlignment(SwingConstants.CENTER);
			lblBrisanjeOblika.setFont(new Font("Courier New", Font.BOLD, 20));
			contentPanel.add(lblBrisanjeOblika, BorderLayout.NORTH);
		}
		{
			JLabel lblIzbor = new JLabel("Are you sure?");
			lblIzbor.setFont(new Font("Courier New", Font.BOLD, 15));
			lblIzbor.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblIzbor, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						potvrda = true;
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}

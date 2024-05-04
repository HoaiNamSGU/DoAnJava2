package controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import view.BieuDoThongKe;
import view.ThongKeView;

public class ThongKeMouseListener implements MouseListener {
	private ThongKeView thongKeView;

	public ThongKeMouseListener(ThongKeView tk) {
		this.thongKeView = tk;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel clickedLabel = (JLabel) e.getSource();
		clickedLabel.setBackground(Color.BLUE);

		String labelText = clickedLabel.getText();
		if (labelText.equals("Sản Phẩm")) {
			thongKeView.panel_Center.setVisible(false);
			thongKeView.panel_Center.removeAll();
			thongKeView.panel_Center.add(new BieuDoThongKe());
			thongKeView.panel_Center.setVisible(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.BieuDoThongKe;
import view.ThongKeView;

public class ThongKeActionListener implements ActionListener {
	private ThongKeView ThongKeView;

	public ThongKeActionListener(ThongKeView tk) {
		this.ThongKeView = tk;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(ThongKeView.comboBox_ThoiGian)
				|| e.getSource().equals(ThongKeView.comboBox_CongViec)) {
			// Cập nhật định dạng cho combobox Ngày bắt đầu và Ngày kết thúc
			ThongKeView.updateDateFormats();
			updateData();
		}
		if (e.getSource().equals(ThongKeView.comboBox_NgayBD)) {
			int StartDate = ThongKeView.comboBox_NgayBD.getSelectedIndex();
			int EndDate = ThongKeView.comboBox_NgayKT.getSelectedIndex();
			if (StartDate > 0) {
				if(StartDate > EndDate)
					ThongKeView.comboBox_NgayBD.setSelectedIndex(EndDate);
				updateData();
			}
			

		}
		if (e.getSource().equals(ThongKeView.comboBox_NgayKT)) {
			int StartDate = ThongKeView.comboBox_NgayBD.getSelectedIndex();
			int EndDate = ThongKeView.comboBox_NgayKT.getSelectedIndex();
			if (StartDate > 0) {
				if(StartDate > EndDate)
					ThongKeView.comboBox_NgayBD.setSelectedIndex(StartDate);
				updateData();
			}

		}
	}

	public void updateData() {
		String congViec = ThongKeView.comboBox_CongViec.getSelectedItem().toString();
		String StartDay= ThongKeView.comboBox_NgayBD.getSelectedItem().toString();
		String EndDay=ThongKeView.comboBox_NgayKT.getSelectedItem().toString();
		String DateFomart = ThongKeView.dateFormat[ThongKeView.comboBox_ThoiGian.getSelectedIndex()];
		ThongKeView.bieuDoThongKe.Update(congViec, StartDay, EndDay, DateFomart);
		
	}
}
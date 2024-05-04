package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import dao.PhieuNhapDao;
import dao.PhieuXuatDao;
import model.PhieuNhap;
import model.PhieuXuat;

public class BieuDoThongKe extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultCategoryDataset dataset2;
	private DefaultCategoryDataset dataset1;

	public BieuDoThongKe() {
		// Tạo bộ dữ liệu mẫu cho biểu đồ
		dataset1 = new DefaultCategoryDataset();
		dataset2 = new DefaultCategoryDataset();

		// Tạo biểu đồ đường từ bộ dữ liệu
		JFreeChart chart = createChart(dataset1, dataset2);
		setLayout(new GridLayout(1, 1, 0, 0));

		// Tạo một panel chứa biểu đồ
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(900, 700)); // Đặt kích thước của biểu đồ
		add(chartPanel); // Thêm biểu đồ vào panel
	}

	public JFreeChart createChart(DefaultCategoryDataset dataset1, DefaultCategoryDataset dataset2) {
	    JFreeChart chart = ChartFactory.createLineChart("Số lượng và Tổng giá trị", // Tiêu đề biểu đồ
	            "Thời gian", // Nhãn trục x
	            "Giá trị", // Nhãn trục y
	            dataset2, // Bộ dữ liệu cho đường 1
	            PlotOrientation.VERTICAL, // Hướng của biểu đồ
	            true, // Hiển thị chú thích
	            true, // Hiển thị tiêu đề
	            false // Không hiển thị URL
	    );

	    // Lấy plot của biểu đồ
	    CategoryPlot plot = (CategoryPlot) chart.getPlot();

	    // Tạo trục y thứ hai
	    NumberAxis yAxis2 = new NumberAxis("Tổng số lượng");
	    plot.setRangeAxis(1, yAxis2);
	    plot.setDataset(1, dataset1); // Sử dụng dataset1 cho đường biểu đồ thứ hai
	    plot.mapDatasetToRangeAxis(1, 1);

	    // Chỉnh sửa màu sắc của đường biểu đồ
	    LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
	    renderer1.setSeriesPaint(0, Color.BLUE); // Chỉnh màu sắc cho đường biểu đồ dataset1
	    plot.setRenderer(1, renderer1); // Ánh xạ renderer1 với đường biểu đồ thứ hai

	    // Chỉnh sửa chú thích
	    plot.getRenderer().setSeriesVisibleInLegend(0, true); // Hiển thị chú thích cho đường biểu đồ dataset1
	    plot.getRenderer().setSeriesVisibleInLegend(1, true); // Hiển thị chú thích cho đường biểu đồ dataset2

	    return chart;
	}


	public DefaultCategoryDataset setData(String CongViec, String type, String startDateStr, String endDateStr,
			String dateFormatStr) {
		ArrayList<?> data = null; // Khai báo một ArrayList không xác định kiểu dữ liệu cụ thể

		// Kiểm tra công việc là nhập hay xuất để chọn danh sách dữ liệu phù hợp
		if (CongViec.equals("Nhập Hàng")) {
			data = PhieuNhapDao.getInstance().selectAll();// Thay thế bằng danh sách dữ liệu nhập
		} else if (CongViec.equals("Xuất Hàng")) {
			data = PhieuXuatDao.getInstance().selectAll();// Thay thế bằng danh sách dữ liệu xuất
		} else {
			// Trường hợp không xác định, trả về null
			return null;
		}

		// Tạo một HashMap để lưu tổng số lượng hoặc tổng giá tiền cho mỗi ngày
		HashMap<String, Integer> tongSoLuongTheoNgay = new HashMap<>();
		HashMap<String, Double> tongGiaTienTheoNgay = new HashMap<>();

		// Định dạng ngày
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr, Locale.getDefault());

		// Chuyển đổi chuỗi ngày tháng thành đối tượng Date
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = dateFormat.parse(startDateStr);
			endDate = dateFormat.parse(endDateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// Tính tổng số lượng hoặc tổng giá tiền cho mỗi ngày trong khoảng từ ngày bắt
		// đầu đến ngày kết thúc
		for (Object dt : data) {
			Date ngay = null;

			// Xác định ngày tương ứng trong phiếu nhập hoặc xuất
			if (dt instanceof PhieuNhap) {
				ngay = ((PhieuNhap) dt).getNgayNhap();
			} else if (dt instanceof PhieuXuat) {
				ngay = ((PhieuXuat) dt).getNgayXuat();
			}
			// Kiểm tra xem ngày có nằm trong khoảng từ ngày bắt đầu đến ngày kết thúc không
			if (ngay != null && ngay.after(startDate) || ngay.before(endDate)) {
				String ngayStr = dateFormat.format(ngay);
				int soLuong = 0;
				double giaTien = 0.0;
				// Xác định số lượng và giá tiền tương ứng
				if (dt instanceof PhieuNhap) {
					soLuong = ((PhieuNhap) dt).getTongSoLuong();
					giaTien = ((PhieuNhap) dt).getTongTien();
				} else if (dt instanceof PhieuXuat) {
					soLuong = ((PhieuXuat) dt).getTongSoLuong();
					giaTien = ((PhieuXuat) dt).getTongTien();
				}

				// Lưu tổng số lượng hoặc tổng giá tiền cho mỗi ngày
				if (type.equals("Số lượng")) {
					tongSoLuongTheoNgay.put(ngayStr, tongSoLuongTheoNgay.getOrDefault(ngayStr, 0) + soLuong);

				} else if (type.equals("Giá trị")) {
					tongGiaTienTheoNgay.put(ngayStr, tongGiaTienTheoNgay.getOrDefault(ngayStr, 0.0) + giaTien);
				}
			}
		}

		// Tạo một DefaultCategoryDataset để chứa dữ liệu mới
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		// Thêm dữ liệu vào dataset từ HashMap dựa trên loại tính toán
		if (type.equals("Số lượng")) {
			for (String ngayStr : tongSoLuongTheoNgay.keySet()) {
				int tongSoLuong = tongSoLuongTheoNgay.get(ngayStr);
				dataset.addValue(tongSoLuong, "Số lượng", ngayStr);

			}
		} else if (type.equals("Giá trị")) {
			for (String ngayStr : tongGiaTienTheoNgay.keySet()) {
				double tongGiaTien = tongGiaTienTheoNgay.get(ngayStr);
				dataset.addValue(tongGiaTien, "Giá trị", ngayStr);
			}
		}

		return dataset;
	}

	public void Update(String CongViec, String startDateStr, String endDateStr, String dateFormatStr) {
		dataset1 = setData(CongViec, "Số lượng", startDateStr, endDateStr, dateFormatStr);
		dataset2 = setData(CongViec, "Giá trị", startDateStr, endDateStr, dateFormatStr);
		removeAll();
		JFreeChart chart = createChart(dataset1, dataset2);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(900, 700)); // Đặt kích thước của biểu đồ
		add(chartPanel);
	}
}
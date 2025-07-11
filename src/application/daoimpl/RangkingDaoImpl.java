package application.daoimpl;

import application.dao.RangkingDao;
import application.models.RangkingModel;
import application.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RangkingDaoImpl implements RangkingDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;

    public RangkingDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

     @Override
    public List<RangkingModel> findRangking() {
        List<RangkingModel> rangkingList = new ArrayList<>();

        try {
            query = "SELECT " +
                    "p.nama AS nama_pelanggan, p.batch, p.kursus, " +
                    "ROUND(SUM( " +
                    "  CASE " +
                    "    WHEN c.type = 'benefit' THEN (sc.jumlah_bobot / pembagi_table.pembagi) * (c.bobot / total_weight.total) " +
                    "    WHEN c.type = 'cost' THEN (pembagi_table.pembagi / sc.jumlah_bobot) * (c.bobot / total_weight.total) " +
                    "  END " +
                    "), 4) AS total_nilai, " +
                    "RANK() OVER (ORDER BY " +
                    "  SUM( " +
                    "    CASE " +
                    "      WHEN c.type = 'benefit' THEN (sc.jumlah_bobot / pembagi_table.pembagi) * (c.bobot / total_weight.total) " +
                    "      WHEN c.type = 'cost' THEN (pembagi_table.pembagi / sc.jumlah_bobot) * (c.bobot / total_weight.total) " +
                    "    END " +
                    ") DESC " +
                    ") AS peringkat " +

                    "FROM alternatif AS a " +
                    "JOIN peserta AS p ON a.id_peserta = p.id " +
                    "JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "JOIN criteria AS c ON sc.id_kreteria = c.id " +

                    "JOIN ( " +
                    "  SELECT " +
                    "    c.id AS id_kriteria, " +
                    "    CASE " +
                    "      WHEN c.type = 'benefit' THEN MAX(sc.jumlah_bobot) " +
                    "      WHEN c.type = 'cost' THEN MIN(sc.jumlah_bobot) " +
                    "    END AS pembagi " +
                    "  FROM alternatif AS a " +
                    "  JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "  JOIN criteria AS c ON sc.id_kreteria = c.id " +
                    "  GROUP BY c.id, c.type " +
                    ") AS pembagi_table ON pembagi_table.id_kriteria = c.id " +

                    "JOIN (SELECT SUM(bobot) AS total FROM criteria) AS total_weight " +

                    "GROUP BY p.nama, p.batch, p.kursus " +
                    "ORDER BY total_nilai DESC;";

            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                RangkingModel rangking = new RangkingModel();
                rangking.setNamaAlternatif(resultSet.getString("nama_pelanggan"));
                rangking.setTotalNilai(resultSet.getDouble("total_nilai"));
                rangking.setPeringkat(resultSet.getInt("peringkat"));
                rangking.setBatch(resultSet.getString("batch"));
                rangking.setKursus(resultSet.getString("kursus"));
                rangkingList.add(rangking);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return rangkingList;
    }
    
    @Override
    public List<RangkingModel> findRangkingBatchAndKursus(String batch, String kursus) {
        List<RangkingModel> rangkingList = new ArrayList<>();

        try {
            query = "SELECT " +
                    "p.nama AS nama_pelanggan, p.batch, p.kursus, " +
                    "ROUND(SUM( " +
                    "  CASE " +
                    "    WHEN c.type = 'benefit' THEN (sc.jumlah_bobot / pembagi_table.pembagi) * (c.bobot / total_weight.total) " +
                    "    WHEN c.type = 'cost' THEN (pembagi_table.pembagi / sc.jumlah_bobot) * (c.bobot / total_weight.total) " +
                    "  END " +
                    "), 4) AS total_nilai, " +
                    "RANK() OVER (ORDER BY " +
                    "  SUM( " +
                    "    CASE " +
                    "      WHEN c.type = 'benefit' THEN (sc.jumlah_bobot / pembagi_table.pembagi) * (c.bobot / total_weight.total) " +
                    "      WHEN c.type = 'cost' THEN (pembagi_table.pembagi / sc.jumlah_bobot) * (c.bobot / total_weight.total) " +
                    "    END " +
                    ") DESC " +
                    ") AS peringkat " +

                    "FROM alternatif AS a " +
                    "JOIN peserta AS p ON a.id_peserta = p.id " +
                    "JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "JOIN criteria AS c ON sc.id_kreteria = c.id " +

                    "JOIN ( " +
                    "  SELECT " +
                    "    c.id AS id_kriteria, " +
                    "    CASE " +
                    "      WHEN c.type = 'benefit' THEN MAX(sc.jumlah_bobot) " +
                    "      WHEN c.type = 'cost' THEN MIN(sc.jumlah_bobot) " +
                    "    END AS pembagi " +
                    "  FROM alternatif AS a " +
                    "  JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "  JOIN criteria AS c ON sc.id_kreteria = c.id " +
                    "  GROUP BY c.id, c.type " +
                    ") AS pembagi_table ON pembagi_table.id_kriteria = c.id " +

                    "JOIN (SELECT SUM(bobot) AS total FROM criteria) AS total_weight ";

            // Tambahkan filter dinamis
            boolean hasWhere = false;
            if (batch != null && !batch.isEmpty()) {
                query += "WHERE p.batch = ? ";
                hasWhere = true;
            }

            if (kursus != null && !kursus.isEmpty()) {
                query += (hasWhere ? "AND " : "WHERE ") + "p.kursus = ? ";
            }

            query += "GROUP BY p.nama, p.batch, p.kursus ORDER BY total_nilai DESC;";

            pstmt = dbConnection.prepareStatement(query);

            int paramIndex = 1;
            if (batch != null && !batch.isEmpty()) {
                pstmt.setString(paramIndex++, batch);
            }
            if (kursus != null && !kursus.isEmpty()) {
                pstmt.setString(paramIndex++, kursus);
            }

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                RangkingModel rangking = new RangkingModel();
                rangking.setNamaAlternatif(resultSet.getString("nama_pelanggan"));
                rangking.setTotalNilai(resultSet.getDouble("total_nilai"));
                rangking.setPeringkat(resultSet.getInt("peringkat"));
                rangking.setBatch(resultSet.getString("batch"));
                rangking.setKursus(resultSet.getString("kursus"));
                rangkingList.add(rangking);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return rangkingList;
    }


    private void closeStatement() {
        try {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

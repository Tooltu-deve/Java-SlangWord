# Slang Dictionary

1. MSSV - Họ tên - Email: 24127578
2. Họ tên: Nguyễn Tấn Tú
3. Email: tunguyen19606@gmail.com
4. Link demo YouTube: https://youtu.be/Ci9o8kgSGBs
5. Phần trăm tham khảo: 30%

## Yêu cầu
- JDK/JRE **11 trở lên**

## Chạy bằng file jar
Mở terminal **tại thư mục chứa folder `data/`** (thư mục gốc project), rồi chạy:
```bash
java -jar SlangDictionary.jar
```

## Biên dịch và chạy từ mã nguồn
```bash
javac --release 11 -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Tạo lại file jar:
```bash
jar cfe SlangDictionary.jar Main -C out .
```

## Dữ liệu
| File | Vai trò |
|---|---|
| `data/slang.txt` | Danh sách gốc, chỉ đọc (dùng cho chức năng Reset) |
| `data/dictionary.dat` | Từ điển đã index, tự tạo ở lần chạy đầu và lưu sau mỗi lần thêm/sửa/xoá |
| `data/history.txt` | Lịch sử tìm kiếm, tự tạo |

Lần chạy đầu app đọc `slang.txt` rồi tạo `dictionary.dat`; các lần sau nạp thẳng từ `dictionary.dat`.
Muốn chạy lại từ đầu thì xoá `dictionary.dat` và `history.txt`.

## Chức năng
Menu bên trái gồm 5 trang:
- **Tìm kiếm**: tìm theo slang hoặc theo definition (1, 2)
- **Lịch sử**: các slang đã tìm (3)
- **Quản lý slang**: thêm, sửa, xoá, reset danh sách gốc (4–7)
- **On this day**: slang ngẫu nhiên của ngày (8)
- **Đố vui**: đố slang → nghĩa và nghĩa → slang (9, 10)

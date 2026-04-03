# 🎮 Tamagotchi 2.0 - Design Pattern Demo

> **Ứng dụng web minh họa sức mạnh của State Pattern và Strategy Pattern trong việc xây dựng hệ thống game có logic phức tạp**

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-green?style=flat-square)
![Design Patterns](https://img.shields.io/badge/Design%20Patterns-State%20%2B%20Strategy-blue?style=flat-square)

---

## 📋 Mục lục

- [Giới thiệu](#-giới-thiệu)
- [Vấn đề cần giải quyết](#-vấn-đề-cần-giải-quyết)
- [Giải pháp với Design Patterns](#-giải-pháp-với-design-patterns)
- [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
- [Sức mạnh của State Pattern](#-sức-mạnh-của-state-pattern)
- [Sức mạnh của Strategy Pattern](#-sức-mạnh-của-strategy-pattern)
- [Cách chạy dự án](#-cách-chạy-dự-án)
- [Demo kịch bản](#-demo-kịch-bản)
- [Mở rộng](#-mở-rộng)

---

## 🌟 Giới thiệu

**Tamagotchi 2.0** là một ứng dụng web mô phỏng trò chơi nuôi thú ảo cổ điển, được xây dựng nhằm minh họa cách sử dụng **State Pattern** và **Strategy Pattern** để quản lý hành vi phức tạp của đối tượng trong game.

### 🎯 Mục tiêu
- Tạo một ứng dụng có **logic nghiệp vụ rõ ràng** và **dễ mở rộng**
- Minh họa cách **tách biệt hành vi** (behavior) khỏi đối tượng chính
- Cho phép **thêm state và action mới** mà không cần sửa code cũ
- Xử lý **chuyển đổi trạng thái tự động** dựa trên điều kiện

### 🔧 Tech Stack
- **Backend**: Java 21 + Spring Boot 3.2.5
- **Frontend**: HTML5 + Tailwind CSS + Vanilla JavaScript
- **Architecture**: RESTful API với Design Patterns

---

## ⚠️ Vấn đề cần giải quyết

### Bài toán

Trong một game nuôi thú ảo, thú cưng có nhiều trạng thái khác nhau (vui vẻ, đói, ốm) và mỗi trạng thái phản ứng khác nhau với các hành động (cho ăn, chơi, chữa bệnh). Nếu không sử dụng Design Pattern, code có thể trở nên rối rắm:

```java
// ❌ CODE TỒI - Sử dụng if-else lồng nhau
public class VirtualPet {
    private String state = "HAPPY";
    private int fullness = 100;
    
    public void feed() {
        if (state.equals("HAPPY")) {
            fullness += 30;
            System.out.println("Pet eats happily");
        } else if (state.equals("HUNGRY")) {
            fullness += 30;
            if (fullness >= 80) {
                state = "HAPPY";
                System.out.println("Pet is happy again");
            }
        } else if (state.equals("SICK")) {
            System.out.println("Pet refuses food, needs medicine");
        }
    }
    
    public void play() {
        if (state.equals("HAPPY")) {
            fullness -= 40;
            if (fullness < 50) {
                state = "HUNGRY";
                System.out.println("Pet becomes hungry");
            }
        } else if (state.equals("HUNGRY")) {
            state = "SICK";
            System.out.println("Pet collapses from exhaustion");
        } else if (state.equals("SICK")) {
            System.out.println("Pet is too weak to play");
        }
    }
    
    public void heal() {
        // More nested if-else...
    }
}
```

### ⛔ Vấn đề của cách tiếp cận này:

1. **Code khó đọc**: Logic nghiệp vụ bị phân tán trong các điều kiện if-else lồng nhau
2. **Khó bảo trì**: Thêm trạng thái mới (ví dụ: "SLEEPING") phải sửa tất cả các method
3. **Vi phạm Open/Closed Principle**: Phải sửa code cũ để thêm tính năng mới
4. **Khó test**: Không thể test riêng logic của từng trạng thái
5. **Code trùng lặp**: Cùng một logic kiểm tra điều kiện xuất hiện nhiều nơi

---

## 💡 Giải pháp với Design Patterns

### ✨ Sử dụng kết hợp State + Strategy Pattern

```java
// ✅ CODE TỐT - Sử dụng Design Patterns
public class VirtualPet {
    private PetState state;  // State Pattern
    
    public void apply(PetStrategy strategy) {  // Strategy Pattern
        state.handle(this, strategy);  // Ủy thác xử lý cho state hiện tại
    }
}
```

**Kết quả**: Code ngắn gọn, rõ ràng và dễ mở rộng!

---

## 🏗️ Kiến trúc hệ thống

### 📊 Class Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         VirtualPet                              │
│                        (Context)                                │
├─────────────────────────────────────────────────────────────────┤
│ - state: PetState                                               │
│ - fullness: int                                                 │
│ - happiness: int                                                │
├─────────────────────────────────────────────────────────────────┤
│ + apply(strategy: PetStrategy): void                            │
│ + setState(state: PetState): void                               │
└───────────────┬─────────────────────────────────────────────────┘
                │
                │ delegates to
                ▼
┌───────────────────────────────────┐      ┌──────────────────────────┐
│      <<interface>>                │      │     <<interface>>        │
│        PetState                   │      │      PetStrategy         │
├───────────────────────────────────┤      ├──────────────────────────┤
│ + handle(pet, strategy): String   │      │ + execute(pet): void     │
│ + getName(): String               │      │ + getName(): String      │
│ + getEmoji(): String              │      └──────────────────────────┘
└───────────┬───────────────────────┘                  △
            △                                          │
            │                                          │ implements
   implements                                          │
            │                             ┌────────────┼────────────┐
  ┌─────────┼─────────────┐               │            │            │
  │         │             │        ┌──────────┐ ┌────────────┐ ┌───────────┐
┌─────┐ ┌─────────┐ ┌──────────┐  │  Feed    │ │   Play     │ │   Heal    │
│Happy│ │ Hungry  │ │  Sick    │  │ Strategy │ │  Strategy  │ │ Strategy  │
│State│ │ State   │ │  State   │  └──────────┘ └────────────┘ └───────────┘
└─────┘ └─────────┘ └──────────┘   +30 full     -40 full       Heals pet
  🐶        🥺          🤒
```

### 🔄 Luồng xử lý

```
┌─────────┐  POST /api/pet/action?actionType=PLAY   ┌───────────────┐
│ Client  │ ────────────────────────────────────────>│PetController  │
│ (HTML)  │                                          │ (REST API)    │
└─────────┘                                          └───────┬───────┘
                                                             │
                                                             │ 1. Create PlayStrategy
                                                             ▼
                                            ┌────────────────────────────────┐
                                            │   pet.apply(playStrategy)      │
                                            └───────────┬────────────────────┘
                                                        │
                                      2. Delegate to current state
                                                        ▼
                          ┌─────────────────────────────────────────────────┐
                          │     currentState.handle(pet, playStrategy)      │
                          └─────────────────────────────────────────────────┘
                                                        │
                          ┌─────────────────────────────┼─────────────────────────────┐
                          │                             │                             │
              If state = HappyState       If state = HungryState        If state = SickState
                          │                             │                             │
                          ▼                             ▼                             ▼
          ┌──────────────────────────┐   ┌──────────────────────────┐   ┌──────────────────────────┐
          │ Execute strategy         │   │ Transition to SickState  │   │ Refuse action            │
          │ fullness -= 40           │   │ "Too hungry to play!"    │   │ "Too weak to play!"      │
          │ Check: fullness < 50?    │   └──────────────────────────┘   └──────────────────────────┘
          │   → Transition to Hungry │
          └──────────────────────────┘
```

---

## 🎭 Sức mạnh của State Pattern

### 📚 Định nghĩa

> **State Pattern** cho phép một object thay đổi hành vi của nó khi trạng thái nội bộ thay đổi. Object sẽ trông như thể đã thay đổi class.

### 💪 Ưu điểm trong dự án

#### 1️⃣ **Tách biệt logic của từng trạng thái**

Mỗi state là một class độc lập, dễ đọc và dễ test:

```java
// HappyState.java - Chỉ chứa logic của trạng thái vui vẻ
public class HappyState implements PetState {
    @Override
    public String handle(VirtualPet pet, PetStrategy strategy) {
        if (strategy instanceof PlayStrategy) {
            strategy.execute(pet);
            if (pet.getFullness() < 50) {
                pet.setState(new HungryState());  // Auto transition
            }
        } else if (strategy instanceof FeedStrategy) {
            strategy.execute(pet);
        } else {
            return "Pet is healthy, refuses medicine";
        }
    }
}
```

#### 2️⃣ **Tự động chuyển đổi trạng thái**

State tự quyết định khi nào chuyển sang state khác:

```java
// Không cần if-else tập trung, mỗi state tự quản lý transition
if (pet.getFullness() < 50) {
    pet.setState(new HungryState());  // Happy → Hungry
}
```

#### 3️⃣ **Dễ dàng thêm state mới**

Muốn thêm trạng thái "Sleeping"? Chỉ cần tạo class mới:

```java
public class SleepingState implements PetState {
    @Override
    public String handle(VirtualPet pet, PetStrategy strategy) {
        return "Pet is sleeping, wake it up first!";
    }
}
```

Không cần sửa code của HappyState, HungryState, SickState!

#### 4️⃣ **Tuân thủ Open/Closed Principle**

- **Open for extension**: Thêm state mới dễ dàng
- **Closed for modification**: Không sửa code cũ

### 🎯 Bảng chuyển đổi trạng thái

| Trạng thái hiện tại | Hành động | Kết quả | Trạng thái mới |
|---------------------|-----------|---------|----------------|
| 🐶 HAPPY | Play 🎾 | Fullness < 50 | 🥺 HUNGRY |
| 🐶 HAPPY | Feed 🍔 | Fullness +30 | 🐶 HAPPY |
| 🥺 HUNGRY | Feed 🍔 | Fullness ≥ 80 | 🐶 HAPPY |
| 🥺 HUNGRY | Play 🎾 | Too weak | 🤒 SICK |
| 🤒 SICK | Heal 💊 | Recovered | 🥺 HUNGRY |
| 🤒 SICK | Feed/Play | Refused | 🤒 SICK |

---

## 🎯 Sức mạnh của Strategy Pattern

### 📚 Định nghĩa

> **Strategy Pattern** định nghĩa một họ các thuật toán, đóng gói từng thuật toán và làm chúng có thể thay thế lẫn nhau. Strategy cho phép thuật toán thay đổi độc lập với client sử dụng nó.

### 💪 Ưu điểm trong dự án

#### 1️⃣ **Đóng gói hành động thành object**

Mỗi action là một class độc lập:

```java
public class FeedStrategy implements PetStrategy {
    @Override
    public void execute(VirtualPet pet) {
        pet.changeStats(+30, +5);  // Fullness +30, Happiness +5
    }
}

public class PlayStrategy implements PetStrategy {
    @Override
    public void execute(VirtualPet pet) {
        pet.changeStats(-40, +20);  // Fullness -40, Happiness +20
    }
}
```

#### 2️⃣ **Dễ dàng thêm action mới**

Muốn thêm action "Sleep"? Chỉ cần:

```java
public class SleepStrategy implements PetStrategy {
    @Override
    public void execute(VirtualPet pet) {
        pet.changeStats(+10, +30);  // Restore energy while sleeping
    }
}
```

Không cần sửa VirtualPet hay các state hiện có!

#### 3️⃣ **Tái sử dụng logic**

Các strategy có thể được sử dụng ở nhiều nơi:

```java
// Có thể dùng lại strategy cho nhiều pet khác nhau
PetStrategy feed = new FeedStrategy();
pet1.apply(feed);
pet2.apply(feed);
```

#### 4️⃣ **Linh hoạt trong runtime**

Có thể thay đổi strategy khi chạy chương trình:

```java
// Controller tự động chọn strategy dựa vào request
PetStrategy strategy = switch (actionType) {
    case "FEED" -> new FeedStrategy();
    case "PLAY" -> new PlayStrategy();
    case "HEAL" -> new HealStrategy();
};
pet.apply(strategy);
```

### 📊 So sánh trước và sau khi dùng Strategy Pattern

| Tiêu chí | Không dùng Pattern | Dùng Strategy Pattern |
|----------|-------------------|----------------------|
| **Thêm action mới** | Sửa VirtualPet class | Tạo class mới |
| **Tái sử dụng logic** | Copy-paste code | Dùng lại strategy object |
| **Test** | Test toàn bộ VirtualPet | Test từng strategy riêng |
| **Code rõ ràng** | if-else phức tạp | Mỗi strategy một class |

---

## 🤝 Kết hợp State + Strategy

### 🔥 Sức mạnh khi kết hợp

Khi kết hợp cả hai pattern, ta có một kiến trúc cực kỳ linh hoạt:

```java
// Context chỉ cần một method duy nhất!
public void apply(PetStrategy strategy) {
    state.handle(this, strategy);  // State quyết định cách xử lý strategy
}
```

**Điều kỳ diệu**:
- **State** quyết định **CÓ THỰC HIỆN** strategy hay không
- **Strategy** quyết định **LÀM GÌ** khi được thực hiện

### 🎬 Ví dụ thực tế

```java
// State kiểm soát việc có cho phép thực hiện strategy không
public class SickState implements PetState {
    @Override
    public String handle(VirtualPet pet, PetStrategy strategy) {
        if (strategy instanceof HealStrategy) {
            strategy.execute(pet);  // ✅ Cho phép
            pet.setState(new HungryState());
        } else {
            // ❌ Từ chối: State kiểm soát strategy nào được phép
            return "Pet is sick, only accepts medicine!";
        }
    }
}
```

### 📈 Lợi ích kết hợp

1. **Tách biệt hoàn toàn**: State quản lý điều kiện, Strategy quản lý hành động
2. **Dễ mở rộng theo 2 chiều**: Thêm state hoặc strategy đều dễ dàng
3. **Code DRY**: Không lặp lại logic
4. **Dễ đọc**: Logic rõ ràng, tuân theo Single Responsibility

---

## 🚀 Cách chạy dự án

### 📦 Yêu cầu hệ thống

- Java 21+
- Maven 3.6+

### ▶️ Chạy ứng dụng

```bash
# Clone repository
cd d:\TU HOC JAVA\Tamagotchi

# Chạy ứng dụng
mvn spring-boot:run
```

### 🌐 Truy cập

Mở trình duyệt tại: **http://localhost:8080**

### 🔌 API Endpoints

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/pet/status` | Lấy trạng thái hiện tại của pet | - |
| POST | `/api/pet/action` | Thực hiện hành động | `actionType`: FEED, PLAY, HEAL |
| POST | `/api/pet/reset` | Reset pet về trạng thái ban đầu | - |

---

## 🎮 Demo kịch bản

### Kịch bản 1: Happy → Hungry

```
1. Trạng thái ban đầu: HAPPY 🐶 (Fullness: 100)
2. Click "Play" → Fullness giảm xuống 60
3. Click "Play" lần 2 → Fullness xuống 20
4. 🚨 Tự động chuyển sang HUNGRY 🥺
   Message: "Playing too much made your pet hungry!"
```

### Kịch bản 2: Hungry → Sick

```
1. Trạng thái: HUNGRY 🥺 (Fullness: 20)
2. Click "Play" khi đang đói
3. 🚨 Tự động chuyển sang SICK 🤒
   Message: "Your pet is too hungry to play and becomes sick!"
```

### Kịch bản 3: Recovery Flow

```
1. Trạng thái: SICK 🤒
2. Click "Feed" → ❌ Từ chối: "Pet refuses food, needs medicine!"
3. Click "Heal" → ✅ Chữa khỏi
4. 🚨 Tự động chuyển sang HUNGRY 🥺
   Message: "After recovery, your pet feels hungry"
5. Click "Feed" 3 lần
6. 🚨 Tự động chuyển về HAPPY 🐶
   Message: "Your pet is full and happy again!"
```

---

## 🔧 Mở rộng

### ➕ Thêm state mới

```java
// 1. Tạo class state mới
public class TiredState implements PetState {
    @Override
    public String handle(VirtualPet pet, PetStrategy strategy) {
        if (strategy instanceof SleepStrategy) {
            strategy.execute(pet);
            pet.setState(new HappyState());
            return "Pet wakes up refreshed!";
        }
        return "Pet is too tired, needs rest";
    }
    
    @Override
    public String getEmoji() { return "😴"; }
}

// 2. Thêm transition từ state khác
// Trong HappyState:
if (pet.getHappiness() < 30) {
    pet.setState(new TiredState());
}
```

### ➕ Thêm strategy mới

```java
// 1. Tạo strategy mới
public class BathStrategy implements PetStrategy {
    @Override
    public void execute(VirtualPet pet) {
        pet.changeStats(0, +15);  // Happiness +15
    }
    
    @Override
    public String getName() { return "Bath 🛁"; }
}

// 2. Thêm vào Controller
case "BATH" -> new BathStrategy();
```

### 🎨 Thêm UI cho action mới

```html
<button onclick="performAction('BATH')">
    <span class="text-2xl">🛁</span>
    <span>Bath</span>
</button>
```

**Không cần sửa code cũ!** 🎉

---

## 📚 Kết luận

### ✅ Ưu điểm của giải pháp

1. **Code sạch**: Mỗi class có trách nhiệm rõ ràng
2. **Dễ mở rộng**: Thêm state/strategy mà không sửa code cũ
3. **Dễ test**: Test riêng từng state và strategy
4. **Dễ bảo trì**: Bug dễ xác định và sửa
5. **Tuân thủ SOLID**: Đặc biệt là Open/Closed và Single Responsibility

### 🎯 Bài học rút ra

> **"Good code is not about making things work. It's about making things easy to change."**

State Pattern và Strategy Pattern không chỉ giúp code hoạt động đúng, mà còn giúp code **dễ thay đổi** và **dễ mở rộng** trong tương lai.

---

## 📞 Liên hệ & Đóng góp

Dự án này được tạo để minh họa Design Patterns trong thực tế. Nếu bạn có ý tưởng cải thiện, hãy mở Pull Request!

---

<div align="center">

**Made with ❤️ using State Pattern + Strategy Pattern**

*"Design Patterns are not just about code, they're about communication and thinking"*

</div>

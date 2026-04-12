import numpy as np
import matplotlib.pyplot as plt
from itertools import combinations

# ===== ВВОД ДАННЫХ =====
print("Тип задачи (max/min):")
goal = input().strip()

print("Введите коэффициенты целевой функции (c1 c2):")
c1, c2 = map(float, input().split())
objective = (c1, c2)

print("Количество ограничений:")
n = int(input())

constraints = []
print("Формат: a b знак c  (например: 1 2 <= 8)")

for i in range(n):
    print(f"Ограничение {i+1}:")
    parts = input().split()
    a = float(parts[0])
    b = float(parts[1])
    sign = parts[2]
    c = float(parts[3])
    constraints.append((a, b, c, sign))

# Масштаб
print("Введите максимум по оси X:")
x_max = float(input())

print("Введите максимум по оси Y:")
y_max = float(input())

print("Шаг сетки:")
grid_step = float(input())

# =======================

def intersection(c1, c2):
    a1, b1, c1_val, _ = c1
    a2, b2, c2_val, _ = c2
    A = np.array([[a1, b1], [a2, b2]])
    B = np.array([c1_val, c2_val])
    if abs(np.linalg.det(A)) < 1e-6:
        return None
    return np.linalg.solve(A, B)

def is_valid(point):
    x, y = point
    if x < 0 or y < 0:
        return False
    for a, b, c, sign in constraints:
        val = a*x + b*y
        if sign == "<=" and val > c + 1e-6:
            return False
        if sign == ">=" and val < c - 1e-6:
            return False
    return True

# === Поиск пересечений ===
points = []
for c1, c2 in combinations(constraints, 2):
    p = intersection(c1, c2)
    if p is not None and is_valid(p):
        points.append(p)

# Пересечения с осями
for a, b, c, sign in constraints:
    if b != 0:
        p = (0, c / b)
        if is_valid(p):
            points.append(p)
    if a != 0:
        p = (c / a, 0)
        if is_valid(p):
            points.append(p)

points = np.array(points)

# === Оптимизация ===
best_value = None
best_point = None

for x, y in points:
    value = objective[0]*x + objective[1]*y
    if best_value is None:
        best_value = value
        best_point = (x, y)
    else:
        if goal == "max" and value > best_value:
            best_value = value
            best_point = (x, y)
        if goal == "min" and value < best_value:
            best_value = value
            best_point = (x, y)

# === ВИЗУАЛИЗАЦИЯ ===
x = np.linspace(0, x_max, 400)

plt.figure(figsize=(8, 8))

for a, b, c, sign in constraints:
    if b != 0:
        y = (c - a*x) / b
        plt.plot(x, y, label=f"{a}x+{b}y{sign}{c}")
    else:
        plt.axvline(x=c/a)

X, Y = np.meshgrid(np.linspace(0, x_max, 200),
                   np.linspace(0, y_max, 200))

mask = np.ones_like(X, dtype=bool)

for a, b, c, sign in constraints:
    if sign == "<=":
        mask &= (a*X + b*Y <= c)
    else:
        mask &= (a*X + b*Y >= c)

plt.contourf(X, Y, mask, levels=[0.5, 1], alpha=0.3)

if len(points) > 0:
    plt.scatter(points[:, 0], points[:, 1], color='black')

if best_point:
    plt.scatter(*best_point, color='red', s=100, label="Оптимум")

plt.grid(True)
plt.xticks(np.arange(0, x_max, grid_step))
plt.yticks(np.arange(0, y_max, grid_step))

plt.xlim(0, x_max)
plt.ylim(0, y_max)

plt.legend()
plt.title(f"Оптимум: {best_point}, Z = {best_value:.2f}")
plt.xlabel("x1")
plt.ylabel("x2")

plt.show()

print("\nРЕЗУЛЬТАТ:")
print("Оптимальная точка:", best_point)
print("Значение функции:", best_value)
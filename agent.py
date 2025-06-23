# import numpy as np
# import tensorflow as tf

# class RLAgent:
#     def __init__(self):
#         self.model = self.build_model()

#     def build_model(self):
#         model = tf.keras.Sequential([
#             tf.keras.layers.Dense(128, input_dim=4, activation='relu'),
#             tf.keras.layers.Dense(64, activation='relu'),
#             tf.keras.layers.Dense(1, activation='sigmoid')
#         ])
#         model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
#         return model
    
#     def choose_action(self, state, epsilon):
#         # Epsilon-greedy strategy: Exploration vs Exploitation
#         if np.random.rand() < epsilon:
#             return np.random.choice([0, 1])  # Random action (exploration)
#         else:
#             state = np.array(state).reshape(1, -1)
#             return int(self.model.predict(state) > 0.5)  # Action based on model (exploitation)

#     def learn(self, state, action, reward, next_state, gamma):
#         # Train the model using the experience (state, action, reward, next_state)
#         target = reward + gamma * self.model.predict(next_state.reshape(1, -1))  # Discounted future reward
#         with tf.GradientTape() as tape:
#             current_prediction = self.model(state.reshape(1, -1))
#             loss = tf.keras.losses.mean_squared_error(target, current_prediction)
#         grads = tape.gradient(loss, self.model.trainable_variables)
#         self.model.optimizer.apply_gradients(zip(grads, self.model.trainable_variables))



import numpy as np
import tensorflow as tf
from collections import deque


# class RLAgent:
#     # def __init__(self, num_actions=2):  # Set default actions if needed
#     #     self.num_actions = num_actions
#     #     self.model = self.build_model(num_actions)

#     # def build_model(self, num_actions):
#     #     model = tf.keras.Sequential([
#     #         tf.keras.layers.Dense(128, input_dim=4, activation='relu'),
#     #         tf.keras.layers.Dense(64, activation='relu'),
#     #         tf.keras.layers.Dense(num_actions, activation='softmax')  # Adjust output size
#     #     ])
#     #     model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
#     #     return model
    
#     def __init__(self, state_size, action_size):
#         self.state_size = state_size
#         self.action_size = action_size
#         self.memory = deque(maxlen=2000)  # ✅ Initialize memory
#         self.gamma = 0.95  # Discount factor
#         self.epsilon = 1.0  # Exploration rate
#         self.epsilon_min = 0.01
#         self.epsilon_decay = 0.995
#         self.learning_rate = 0.001
#         self.model = self.build_model()

#     def build_model(self):
#         from tensorflow.keras.models import Sequential
#         from tensorflow.keras.layers import Dense
#         from tensorflow.keras.optimizers import Adam

#         model = Sequential([
#             Dense(24, input_dim=self.state_size, activation='relu'),
#             Dense(24, activation='relu'),
#             Dense(self.action_size, activation='linear')
#         ])
#         model.compile(loss='mse', optimizer=Adam(learning_rate=self.learning_rate))
#         return model

#     def remember(self, state, action, reward, next_state):
#         self.memory.append((state, action, reward, next_state))  # ✅ Store experience
    
#     def choose_action(self, state, epsilon):
#         if np.random.rand() < epsilon:
#             return np.random.choice([0, 1])  # Random action (exploration)
#         else:
#             state = np.array(state).reshape(1, -1)
#             action_probs = self.model.predict(state, verbose=0)  # Suppress verbose output
#             return np.argmax(action_probs)  # Action with highest probability
  

#     # def learn(self, state, action, reward, next_state, gamma):
#     #     target = reward + gamma * np.max(self.model.predict(next_state.reshape(1, -1), verbose=0))

#     #     with tf.GradientTape() as tape:
#     #         current_q_values = self.model(state.reshape(1, -1))
#     #         loss_fn = tf.keras.losses.MeanSquaredError()
#     #         loss = loss_fn(target, current_q_values[0][action])

#     #     grads = tape.gradient(loss, self.model.trainable_variables)
#     #     self.model.optimizer.apply_gradients(zip(grads, self.model.trainable_variables))
#     def learn(self, state, action, reward, next_state, gamma):
#         state = tf.convert_to_tensor(state.reshape(1, -1), dtype=tf.float32)
#         next_state = tf.convert_to_tensor(next_state.reshape(1, -1), dtype=tf.float32)

#         # Compute target Q-value
#         target_q = self.model(next_state, training=False)
#         target = reward + gamma * tf.reduce_max(target_q)

#         with tf.GradientTape() as tape:
#             q_values = self.model(state, training=True)
#             q_action = tf.gather_nd(q_values, [[0, action]])  # Extract Q-value of chosen action

#             # Use TensorFlow's loss function properly
#             loss = tf.keras.losses.MeanSquaredError()(tf.expand_dims(target, 0), tf.expand_dims(q_action, 0))

#         grads = tape.gradient(loss, self.model.trainable_variables)

#         # 🔴 Check if gradients are None before applying them
#         if any(g is None for g in grads):
#             print("⚠️ No valid gradients found. Skipping training step.")
#             return

#         self.model.optimizer.apply_gradients(zip(grads, self.model.trainable_variables))






import os
import pickle
import random
import numpy as np
import tensorflow as tf
from flask import Flask, jsonify, request
from collections import deque,defaultdict
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.optimizers import Adam

# ----- RLAgent Class -----
class RLAgent:
    def __init__(self, state_size, action_size,q_table_file="q_table.pkl"):
        self.state_size = state_size
        self.action_size = action_size
        self.memory = deque(maxlen=10000)
        self.gamma = 0.95  # Discount factor
        self.epsilon = 1.0  # Exploration rate
        self.epsilon_min = 0.01
        self.epsilon_decay = 0.995
        self.learning_rate = 0.001
        self.model = self.build_model()
        self.load_model()
        self.q_table = defaultdict(lambda: np.zeros(self.action_size))  # Initialize Q-table
        self.q_table_file = q_table_file  
        # self.load_q_table()

    def build_model(self):
        model = Sequential([
            Dense(24, input_dim=self.state_size, activation='relu'),
            Dense(24, activation='relu'),
            Dense(self.action_size, activation='linear')
        ])
        model.compile(loss='mse', optimizer=Adam(learning_rate=self.learning_rate))
        return model

    # def choose_action(self, state):
    #     if np.random.rand() < self.epsilon:
    #         return np.random.randint(self.action_size)
    #     state = np.array(state).reshape(1, -1)
    #     action_probs = self.model.predict(state, verbose=0)
    #     return np.argmax(action_probs)


    def choose_action(self, state):
        """ Choose an action and update Q-table within this function """
        state_tuple = tuple(state)  # Convert to hashable tuple
        
        # Initialize state in Q-table if not present
        if state_tuple not in self.q_table:
            self.q_table[state_tuple] = np.zeros(self.action_size)
        
        # Choose action using epsilon-greedy policy
        if np.random.rand() < self.epsilon:
            action = np.random.randint(self.action_size)  # Random action (exploration)
        else:
            action = np.argmax(self.q_table[state_tuple])  # Best action (exploitation)

        # Assume immediate reward is 0 (will be updated later by environment)
        reward = 0
        next_state_tuple = state_tuple  # Assuming next state is same for now

        # Q-learning update: Q(s, a) = Q(s, a) + α [r + γ max_a' Q(s', a') - Q(s, a)]
        best_next_action = np.argmax(self.q_table[next_state_tuple])
        self.q_table[state_tuple][action] += self.learning_rate * (
            reward + self.gamma * self.q_table[next_state_tuple][best_next_action] - self.q_table[state_tuple][action]
        )

        
        self.save_q_table()

        print(f"ℹ️ Updated Q-values for state {state_tuple}: {self.q_table[state_tuple]}")
        return action

    def save_q_table(self):
        """ Save the Q-table to a file """
        with open(self.q_table_file, "wb") as f:
            pickle.dump(dict(self.q_table), f)  # Convert defaultdict to dict before saving
        print("✅ Q-Table Saved!")

    # -------------------------------------------------------------------------------------------------

    def learn(self, state, action, reward, next_state):
        state = np.array(state).reshape(1, -1)
        next_state = np.array(next_state).reshape(1, -1)
        target_q = self.model.predict(next_state, verbose=0)
        target = reward + self.gamma * np.max(target_q)
        q_values = self.model.predict(state, verbose=0)
        q_values[0][action] = target
        self.model.fit(state, q_values, epochs=1, verbose=0)

    def save_model(self, filename="model.h5"):
        self.model.save(filename)

    def load_model(self, filename="model.h5"):
        try:
            # ✅ Explicitly define the loss function while loading
            self.model = tf.keras.models.load_model(filename, compile=False)
            self.model.compile(optimizer="adam", loss=tf.keras.losses.MeanSquaredError())  
            print("✅ Model loaded successfully!")
        except Exception as e:
            print(f"⚠️ Error loading model: {e}. Building a new model...")
            self.model = self.build_model()

    # def save_q_table(self, filename="q_table.pkl"):
    #     try:
    #         with open(filename, "wb") as f:
    #             pickle.dump(self.memory, f)
    #         # self.model.save_weights("model_weights.h5")  # Save model weights separately
    #         print("💾 Q-Table & Model Weights Saved!")
    #     except Exception as e:
    #         print(f"⚠️ Error saving Q-table: {e}")
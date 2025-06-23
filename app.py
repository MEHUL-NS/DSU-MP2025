# from flask import Flask, jsonify, request
# from RLAgent.agent import RLAgent
# import numpy as np

# app = Flask(__name__)

# # Instantiate the RL Agent
# agent = RLAgent()


# @app.route('/')
# def home():
#     return "Server is running"



# @app.route('/get_state', methods=['GET'])
# def get_state():
#     # This would usually come from CloudSim (system state), but here we return a dummy state
#     state = np.random.rand(4).tolist()  # Example: Random state (e.g., CPU and RAM usage)
#     return jsonify(state)

# @app.route('/allocate_vm', methods=['POST'])
# def allocate_vm():
#     try:
#         # Receive the current system state from CloudSim
#         state = request.json.get('state')
#         epsilon = request.json.get('epsilon', 1.0)

#         if state is None:
#             return jsonify({"error": "Missing state in request"}), 400

#         # Convert state to numpy array
#         state = np.array(state)

#         # Get the action from the RL agent
#         action = agent.choose_action(state, epsilon)

#         # Convert NumPy type to native Python type (Fix JSON serialization issue)
#         action = int(action)

#         # Perform VM allocation action
#         response = {"action": action}

#         return jsonify(response)

#     except Exception as e:
#         return jsonify({"error": str(e)}), 500
# if __name__ == '__main__':
#     app.run(debug=True, host='0.0.0.0', port=5000)



import os
from flask import Flask, jsonify, request
from RLAgent.agent import RLAgent
import numpy as np

# app = Flask(__name__)
# agent = RLAgent()


# num_actions = 2  


# @app.route('/')
# def home():
#     return "Server is running"



# @app.route('/allocate_vm', methods=['POST'])
# def allocate_vm():
#     try:
#         state = request.json.get('state')
#         epsilon = request.json.get('epsilon', 1.0)

#         if state is None:
#             return jsonify({"error": "Missing state in request"}), 400
        
#         state = np.array(state)
#         num_hosts = len(state) // 2

#         action = agent.choose_action(state, epsilon)
#         max_attempts = 10  # Prevent infinite loop

#         for _ in range(max_attempts):
#             if action < num_hosts and state[action * 2] < 1.0 and state[action * 2 + 1] < 1.0:
#                 return jsonify({"action": int(action)})
#             action = np.random.randint(0, num_hosts)

#         return jsonify({"error": "No valid host found"}), 400  # No available host found

#     except Exception as e:
#         return jsonify({"error": str(e)}), 500

# if __name__ == '__main__':
#     app.run(debug=True, host='0.0.0.0', port=5000)



# from flask import Flask, jsonify, request
# from RLAgent.agent import RLAgent
# from training import TrainingLoop  # Import training loop
# import numpy as np
# import os

# app = Flask(__name__)
# state_size = 4  # Update based on your environment
# action_size = 2  # Update based on the number of actions
# agent = RLAgent(state_size, action_size)


# # 🔹 If no model exists, train first
# if not os.path.exists("model.h5"):
#     print("No trained model found. Training now...")
#     training_loop = TrainingLoop(agent)
#     training_loop.train()
#     agent.save_model()
# else:
#     agent.load_model()

# @app.route('/')
# def home():
#     return "Server is running"

# @app.route('/allocate_vm', methods=['POST'])
# def allocate_vm():
#     try:
#         state = request.json.get('state')
#         epsilon = request.json.get('epsilon', 1.0)
#         num_hosts = len(state) // 2  # Each host has 2 values (CPU, RAM)

#         if state is None:
#             return jsonify({"error": "Missing state in request"}), 400

#         state = np.array(state)
#         action = agent.choose_action(state, epsilon)

#         # Validate action: Ensure chosen host has enough resources
#         while action >= num_hosts or state[action * 2] >= 1.0 or state[action * 2 + 1] >= 1.0:
#             action = np.random.randint(0, num_hosts)  # Pick another valid host

#         return jsonify({"action": int(action)})
#     except Exception as e:
#         return jsonify({"error": str(e)}), 500

# if __name__ == '__main__':
#     app.run(debug=True, host='0.0.0.0', port=5000)


from training import TrainingLoop

app = Flask(__name__)
state_size = 4
action_size = 2
agent = RLAgent(state_size, action_size)

if not os.path.exists("model.h5"):
    print("No trained model found. Training now...")
    training_loop = TrainingLoop(agent, num_episodes=10, steps_per_episode=100)
    training_loop.train()

@app.route('/')
def home():
    return "Server is running"

# @app.route('/allocate_vm', methods=['POST'])
# def allocate_vm():
#     try:
#         state = request.json.get('state')
#         if state is None:
#             return jsonify({"error": "Missing state in request"}), 400
#         state = np.array(state)
#         action = agent.choose_action(state)
#         return jsonify({"action": int(action)})
#     except Exception as e:
#         return jsonify({"error": str(e)}), 500


@app.route('/allocate_vm', methods=['POST'])
def allocate_vm():
    try:
        state = request.json.get('state')
        if state is None:
            return jsonify({"error": "Missing state in request"}), 400
        
        state = np.array(state)
        action = agent.choose_action(state)

        # 🔹 Save Q-table (or model weights) after each allocation
        agent.save_q_table()  

        return jsonify({"action": int(action)})
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)


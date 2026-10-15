WATCH_PID_FILE := /tmp/patientmanagement-compose-watch.pid
WATCH_LOG_FILE := /tmp/patientmanagement-compose-watch.log

up:
	docker compose up -d --build
	@if [ -f "$(WATCH_PID_FILE)" ] && ps -p "$$(cat "$(WATCH_PID_FILE)")" -o args= 2>/dev/null | grep -Fq "docker compose watch --no-up"; then \
		echo "Compose watch is already running."; \
	else \
		rm -f "$(WATCH_PID_FILE)"; \
		nohup docker compose watch --no-up >"$(WATCH_LOG_FILE)" 2>&1 </dev/null & \
		echo $$! >"$(WATCH_PID_FILE)"; \
		sleep 1; \
		if kill -0 "$$(cat "$(WATCH_PID_FILE)")" 2>/dev/null; then \
			echo "Compose watch is running in the background. Logs: $(WATCH_LOG_FILE)"; \
		else \
			echo "Compose watch failed to start. See $(WATCH_LOG_FILE)."; \
			rm -f "$(WATCH_PID_FILE)"; \
			exit 1; \
		fi; \
	fi

down:
	@if [ -f "$(WATCH_PID_FILE)" ]; then \
		watch_pid=$$(cat "$(WATCH_PID_FILE)"); \
		if ps -p "$$watch_pid" -o args= | grep -Fq "docker compose watch --no-up"; then \
			kill "$$watch_pid"; \
		fi; \
		rm -f "$(WATCH_PID_FILE)"; \
	fi
	docker compose down
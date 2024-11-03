// components/RadarChart.js
import React from "react";
import { Radar } from "react-chartjs-2";
import {
  Chart,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend,
} from "chart.js";

Chart.register(
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
);

const RadarChart = ({ data }) => {
  const chartData = {
    labels: [
      "승진 기회",
      "복지 및 급여",
      "업무와 삶의 균형",
      "사내 문화",
      "경영진",
    ],
    datasets: [
      {
        label: "기업 평균",
        data: [
          data.averagePromotion,
          data.averageSalary,
          data.averageBalance,
          data.averageCulture,
          data.averageManagement,
        ],
        backgroundColor: "rgba(139, 204, 159, 0.7)", // 배경색 변경
        borderColor: "rgba(139, 204, 159, 1)", // 테두리 색상 변경
        borderWidth: 1,
      },
    ],
  };

  const options = {
    scales: {
      r: {
        min: 0,
        max: 5,
        ticks: {
          stepSize: 1,
          display: false, // 숫자 표시하지 않기
        },
        pointLabels: {
          font: {
            size: 14,
          },
        },
      },
    },
    plugins: {
      legend: {
        labels: {
          color: "black", // 범례 텍스트 색상 변경
        },
      },
    },
  };

  return <Radar data={chartData} options={options} />;
};

export default RadarChart;

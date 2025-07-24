import type { UUID } from "crypto";
import type { DataWithPagination, RecipeDto } from "../types/recipe-types";

const API_BASE_URL = "http://localhost:8080/api/v1";

export const fetchAll = async (): Promise<DataWithPagination<RecipeDto[]>> => {
  try {
    const response = await fetch(
      `${API_BASE_URL}/recipe/all?pageNumber=0&pageSize=10`
    );

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error occurred:", error);
    throw error;
  }
};

export const fetchById = async (id: UUID): Promise<RecipeDto> => {
  const response = await fetch(`${API_BASE_URL}/recipe/byId/${id}`);
  if (!response.ok) {
    throw new Error(`Recipe not found! Status: ${response.status}`);
  }
  return response.json();
};
